package cn.zvo.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL; 
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.xnx3.BaseVO;
//import com.xnx3.Lang;
//import com.xnx3.Log;

/**
 * http 、https 网络请求
 * @author 管雷鸣
 */ 
public class Http { 
	public final static String UTF8="UTF-8";
	public final static String GBK="GBK";
	
	/**
	 * 请求方式 - POST 请求
	 */
	public final static String METHOD_POST = "POST";
	/**
	 * 请求方式 - GET 请求
	 */
	public final static String METHOD_GET = "GET";
	/**
	 * 请求方式 - PUT 请求
	 */
	public final static String METHOD_PUT = "PUT";
	
    private String encode; 	//默认编码格式
    private String cookies="";	//每次请求都用自动发送此cookies,请求完毕后自动更新此cookies
    private int timeout = 30000;	//超时时间，默认30秒
    private String sslProtocol = "SSL"; //
    
    
    public String getSslProtocol() {
		return sslProtocol;
	}
    
    /**
     * 设置 https 请求的 SSLContext.getInstance(...)
     * @param sslProtocol 传入如  SSL、TLS、TLSv1.2
     */
	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}

	/**
     * 设置超时时间
     * @param secend 单位：秒
     */
    public void setTimeout(int secend){
    	this.timeout = secend * 1000;
    }
   
    /**
     * 设置好编码类型，若不设置，默认是Java虚拟机当前的文件编码
     * <ul>
     * 		<li>使用时首先会自动获取请求地址的编码，获取编码失败时才会使用此处的编码</li>
     * </ul>
     * @see Http#HttpUtil(String)
     */
    public Http() { 
        this.encode = Charset.defaultCharset().name(); 
    }
    
    /**
     * 设置好编码类型，若不设置则默认是Java虚拟机当前的文件编码
     * @param encode 使用时首先会自动获取请求地址的编码，获取编码失败时才会使用此处的编码<p> {@link Http#UTF8} {@link Http#GBK}
     */
    public Http(String encode) { 
        this.encode = encode; 
    } 
   
    /**
     * 设置默认的响应字符集，若不设置默认是UTF-8编码
     * @param encode 字符编码 ，默认使用UTF-8，传入参数如{@link Http#GBK}
     */ 
    public void setEncode(String encode) { 
        this.encode = encode; 
    } 
    
    /**
     * 获取上次请求完成后获得的Cookies
     * @return cookies 获得到的cookies
     */
    public String getCookies() {
		return cookies;
	}
    
    /**
     * 设置请求时会附带传递的cookies
     * @param cookies {@link #getCookies()}获取到的值
     */
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	
	/**
     * get请求
     * @param url 目标URL地址,传入如 http://www.zvo.cn/index.html
     * @return 响应对象 若是返回null则相应失败抛出异常
	 * @throws IOException 
     */ 
    public Response get(String url) throws IOException{ 
       	return this.send(url, METHOD_GET, new HashMap<String, String>(), null);
    } 
    
    /**
     * GET请求
     * @param url 目标URL地址 ,传入如 http://www.zvo.cn/index.html
     * @param params get传递的参数。当然这个也可以选择不通过此参数传递，而是直接有url携带参数也可以
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response get(String url, Map<String, String> params) throws IOException{ 
		return this.send(url, METHOD_GET, params, null);
    } 
   
    /**
     * get请求
     * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
     * @param params get传递的参数。当然这个也可以选择不通过此参数传递，而是直接有url携带参数也可以
     * @param headers 请求属性
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response get(String url, Map<String, String> params, Map<String, String> headers) throws IOException{ 
		return this.send(url, METHOD_GET, params, headers);
    } 

    /**
     * POST请求
     * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response post(String url) throws IOException{ 
    	return this.send(url, METHOD_POST, new HashMap<String, String>(), null);
    } 

    /**
     * POST请求
     * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
     * @param params 参数集合，post提交哪些参数。如果没有可传入null
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response post(String url, Map<String, String> params) throws IOException { 
		return this.send(url, METHOD_POST, params, null);
    } 
   
    /**
     * POST请求
     * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
     * @param params 参数集合，post提交哪些参数。如果没有可传入null
     * @param headers 请求属性,headers
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response post(String url, Map<String, String> params, Map<String, String> headers) throws IOException{ 
		return this.send(url, METHOD_POST, params, headers);
    } 
    
    /**
     * POST请求 - 发送payload
     * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
     * @param payload 提交的payload内容，可传入null则为不提交payload数据
     * @param headers 请求属性,headers
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response post(String url, String payload, Map<String, String> headers) throws IOException{ 
		return send(url, METHOD_POST, payload, headers);
    }
    
    
    /**
     * PUT 请求
     * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
     * @param payload put提交的payload内容，可传入null则为不提交payload数据
     * @param headers 请求属性,headers
     * @throws IOException 
     */
    public Response put(String url, String payload, Map<String, String> headers) throws IOException {
    	return send(url, METHOD_PUT, payload, headers);
    }

	/**
	 * 将Map转换为URL的请求GET参数
	 * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
	 * @param params 请求参数Map集合
	 * @return 完整的GET方式网址
	 */
	public static String mapToUrl(String url,Map<String, String> params){
		int i = 0; 
		if(url.indexOf("?") > 0){
			i = 1;
		}
		
		StringBuffer param = new StringBuffer(); 
        
        for (String key : params.keySet()) { 
            if (i == 0){
            	param.append("?");
            }else{
            	param.append("&");
            }
            param.append(key).append("=").append(params.get(key)); 
            i++; 
        } 
        url += param; 
        return url;
	}
	
	/**
	 * 将Map参数转变为 URL后的字符组合形势。
	 * @param params Map 
	 * @return 返回url拼接的key、value，返回如： key=value&amp;key=value
	 */
	public static String mapToQueryString(Map<String,String> params){
    	String data = "";
    	StringBuffer param = new StringBuffer(); 
        if(params != null){
        	int i = 0; 
            for (Map.Entry<String, String> entry : params.entrySet()) {  
            	if (i > 0){
                	param.append("&");
                }
                param.append(entry.getKey()).append("=").append(entry.getValue()); 
                i++; 
            }
        }
        if(param.length()>0){
        	data = param.toString();
        }
        return data;
	}
    

    /**
     * 发送请求
     * @param url 目标URL地址,传入如 http://www.zvo.cn/index.html
     * @param method 请求方式，如get、post请求等。传入如 {@link #METHOD_POST}
     * @param params 添加由键值对指定的请求参数
     * @param headers 添加由键值对指定的一般请求属性,headers
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response send(String url, String method, Map<String, String> params, 
            Map<String, String> headers) throws IOException{
    	 if (method.equalsIgnoreCase(METHOD_GET) && params != null) { 
             url = mapToUrl(url, params);
         } 
    	 
    	 StringBuffer paramString = new StringBuffer(); 
    	 if (!method.equalsIgnoreCase(METHOD_GET) && params != null) { 
    		 for (String key : params.keySet()) { 
    			 if(paramString.length() > 0) {
    				 paramString.append("&");  
    			 }
    			 paramString.append(key).append("=").append(params.get(key)); 
    		 }
    	 }
    	 
    	 return send(url, method, paramString.toString(), headers);
    } 
	
    /**
     * 发送请求
     * @param url 目标URL地址,传入如 http://www.zvo.cn/index.html
     * @param method 请求方式，如get、post请求等。传入如 {@link #METHOD_POST}
     * @param data 请求所携带的内容，其实就是payload发送的内容。如果是普通的表单post提交，那这里的值就如： a=1&b=2&c=3
     * @param headers 添加由键值对指定的一般请求属性,headers
     * @return 响应对象
     * @throws IOException 
     */
    public Response send(String url, String method, String data, 
            Map<String, String> headers) throws IOException { 
    	String protocols = getProtocols(url);
    	if(protocols == null || protocols.length() == 0) {
    		//协议未发现
    		Response response = new Response();
    		response.code = -1;
    		response.content = "此url协议未发现，请传入完整的带有http://、https:// 协议的url。 当前传入的url:"+url;
    		return response;
    	}
    	
    	if(protocols.equalsIgnoreCase("http")) {
    		return sendHttp(url, method, data, headers);
    	}else if(protocols.equalsIgnoreCase("https")) {
    		return sendHttps(url, method, data, headers);
    	}else {
    		//协议不支持
    		Response response = new Response();
    		response.code = -1;
    		response.content = "此url协议不支持，url:"+url;
    		return response;
    	}
    }
    
    public Proxy proxy;
    public void setProxy(String address, int port) {
    	proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(address, port));
    }
    
    Response sendHttp(String url, String method, String data, 
            Map<String, String> headers) throws IOException { 
        URL requestURL = new URL(url); 
        
        HttpURLConnection urlConnection = null;
        if(proxy == null) {
        	urlConnection = (HttpURLConnection) requestURL.openConnection(); 
        }else {
        	urlConnection = (HttpURLConnection) requestURL.openConnection(proxy); 
        }
        
        urlConnection.setRequestMethod(method); 
        urlConnection.setDoOutput(true); 
        urlConnection.setDoInput(true); 
        urlConnection.setUseCaches(false); 
        urlConnection.setRequestProperty("Cookie", this.cookies);
        
        if (headers != null) {
        	for (String key : headers.keySet()) { 
                urlConnection.addRequestProperty(key, headers.get(key)); 
//                if(key.equalsIgnoreCase("Content-Type")) {
//                	if(headers.get(key).toLowerCase().indexOf("multipart/form-data") > -1) {
//                		urlConnection.setRequestProperty("Content-Type", headers.get(key));
//                	}
//                }
            } 
        }
   
        if(data != null && data.length() > 0) {
        	// 建立输入流，向指向的URL传入参数
//        	DataOutputStream dos=new DataOutputStream(urlConnection.getOutputStream());
//        	dos.writeBytes(data);
//        	dos.flush();
//        	dos.close();
        	
        	PrintWriter writer = new PrintWriter(urlConnection.getOutputStream());
			writer.print(data);
			writer.flush();
			writer.close();
        }

        return this.makeContent(url, urlConnection); 
    } 
    
    Response sendHttps(String url, String method, String data, 
            Map<String, String> headers) throws IOException { 
//    	System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
    	
    	HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
    	
    	SSLContext sc = null;
		try {
			sc = SSLContext.getInstance(sslProtocol);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
        try {
			sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        
        URL console = new URL(url);
        HttpsURLConnection urlConnection = null;
        if(proxy == null) {
        	urlConnection = (HttpsURLConnection) console.openConnection();
        }else {
        	urlConnection = (HttpsURLConnection) console.openConnection(proxy);
        }
        
        urlConnection.setSSLSocketFactory(sc.getSocketFactory());
        urlConnection.setHostnameVerifier(new TrustAnyHostnameVerifier());
        
        urlConnection.setRequestMethod(method); 
       
//        urlConnection.setUseCaches(false); 
        urlConnection.setRequestProperty("Cookie", this.cookies);
   
        if(data != null && data.length()>0){
        	if(headers == null) {
        		headers = new HashMap<String, String>();
        	}
        	headers.put("Content-Length", data.length()+"");
        }
        if (headers != null) {
        	for (String key : headers.keySet()) { 
                urlConnection.addRequestProperty(key, headers.get(key)); 
            } 
        }
        
        
        urlConnection.setDoInput(true); 
        urlConnection.setDoOutput(true); 
   
        if(data != null && data.length() > 0) {
        	// 建立输入流，向指向的URL传入参数
//        	DataOutputStream dos=new DataOutputStream(urlConnection.getOutputStream());
//        	dos.writeBytes(data);
//        	dos.flush();
//        	dos.close();
        	
        	PrintWriter writer = new PrintWriter(urlConnection.getOutputStream());
			writer.print(data);
			writer.flush();
			writer.close();
        }

        return this.makeContent(url, urlConnection); 
    } 
    
    /**
     * 得到响应对象
     * @param url 目标URL地址，传入如 http://www.zvo.cn/index.html
     * @param urlConnection {@link HttpURLConnection}
     * @return 响应对象
     * @throws IOException IO异常
     */ 
    Response makeContent(String url, HttpURLConnection urlConnection) throws IOException { 
    	urlConnection.setConnectTimeout(this.timeout);
    	urlConnection.setReadTimeout(this.timeout);
        Response httpResponser = new Response(); 
        try { 
        	InputStream in = null;
        	try {
        		in = urlConnection.getInputStream();
        	} catch (IOException ioe) {
        	    if (urlConnection instanceof HttpURLConnection) {
        	        HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
        	        int statusCode = httpConn.getResponseCode();
        	        if (statusCode != 200) {
        	        	in = httpConn.getErrorStream();
        	        }
        	    }
        	    if(in == null) {
        	    	in = new ByteArrayInputStream(ioe.getMessage().getBytes(StandardCharsets.UTF_8));
        	    }
        	}
            
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            httpResponser.outputStream = result;
            httpResponser.headerFields = urlConnection.getHeaderFields();
            
            if(httpResponser.headerFields != null) {
            	List<String> list = httpResponser.headerFields.get("Content-Encoding");
            	if(list != null) {
            		String ce = null;
            		for(int i = 0; i<list.size(); i++) {
                		if(list.get(i) != null && list.get(i).length() > 0) {
                			ce = list.get(i).trim();
                		}
                	}
            		if(ce != null) {
                		//是被压缩的
                		
                		if(ce.equalsIgnoreCase("gzip")) {
                			byte[] bs = Unzip.unGzip(httpResponser.outputStream.toByteArray());
                			httpResponser.content = new String(bs, Charset.forName(this.encode));
                		}else if(ce.equalsIgnoreCase("deflate")) {
                			byte[] bs = Unzip.unDeflate(httpResponser.outputStream.toByteArray());
                			httpResponser.content = new String(bs, Charset.forName(this.encode));
                		}else if(ce.equalsIgnoreCase("br")) {
                			System.out.println("Content-Encoding:br 方式压缩的还没加入解压方法，可联系 mail@xnx3.com 升级");
                		}else {
                			System.out.println("Content-Encoding:"+ce+" 方式压缩的还没加入解压方法，可联系 mail@xnx3.com 升级");
                		}
                	}
            	}
            }
            if(httpResponser.content == null) {
            	httpResponser.content = result.toString(this.encode);
            }
            
            httpResponser.urlString = url; 
            //urlConnection.getHeaderField("Set-Cookie");获取到的COOKIES不全，会将JSESSIONID漏掉，故而采用此中方式
            if(this.cookies == null || this.cookies.equals("")){
            	if(urlConnection.getHeaderFields().get("Set-Cookie") != null){
            		List<String> listS = urlConnection.getHeaderFields().get("Set-Cookie");
            		String cookie = "";
                	if(listS != null){
                        for (int i = 0; i < listS.size(); i++) {
            				cookie = cookie + (cookie.equals("")? "":", ") + listS.get(i);
            			}
                	}else{
                		cookie = urlConnection.getHeaderField("Set-Cookie");
                	}
                	this.cookies=cookie;
                	httpResponser.cookie=this.cookies;
            	}
            }
            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort(); 
            httpResponser.file = urlConnection.getURL().getFile(); 
            httpResponser.host = urlConnection.getURL().getHost(); 
            httpResponser.path = urlConnection.getURL().getPath(); 
            httpResponser.port = urlConnection.getURL().getPort(); 
            httpResponser.protocol = urlConnection.getURL().getProtocol(); 
            httpResponser.query = urlConnection.getURL().getQuery(); 
            httpResponser.ref = urlConnection.getURL().getRef(); 
            httpResponser.userInfo = urlConnection.getURL().getUserInfo(); 
            httpResponser.contentEncoding = this.encode; 
            httpResponser.code = urlConnection.getResponseCode(); 
            httpResponser.message = urlConnection.getResponseMessage(); 
            httpResponser.contentType = urlConnection.getContentType(); 
            httpResponser.method = urlConnection.getRequestMethod(); 
            httpResponser.connectTimeout = urlConnection.getConnectTimeout(); 
            httpResponser.readTimeout = urlConnection.getReadTimeout(); 
            
        } catch (IOException e) {
//        	e.printStackTrace();
        	httpResponser.code = 0;
        	httpResponser.message = e.getMessage();
        	throw e;
        } finally { 
            if (urlConnection != null) 
                urlConnection.disconnect(); 
        } 
        return httpResponser; 
    } 
    
    /**
	 * 获取当前url的协议，返回如 http 、 https 、 ftp 、 file 等
	 * @param url 绝对路径，必须是全的，包含协议的，如 http://www.xnx3.com
	 * @return 自动截取的协议，如 http 。若取不到，则返回null
	 */
	public static String getProtocols(String url){
		if(url == null){
			return null;
		}
		
		if(url.indexOf("//") > 1){
			return url.substring(0, url.indexOf("//") - 1);
		}
		return null;
	}
    
    /**
     * gzip的网页用到
     * @param in 输入流
     * @param charset 编码格式 {@link Http#UTF8} {@link Http#GBK}
     * @return 网页源代码
     */
    public String uncompress(ByteArrayInputStream in,String charset) {
        try {
           GZIPInputStream gInputStream = new GZIPInputStream(in);
           byte[] by = new byte[1024];
           StringBuffer strBuffer = new StringBuffer();
           int len = 0;
           while ((len = gInputStream.read(by)) != -1) {
            strBuffer.append( new String(by, 0, len,charset) );
           }
           return strBuffer.toString();
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取经过GZIP压缩的网页源代码
     * @param requestUrl 请求URL地址
     * @return 网页源代码
     */
    public String getGZIP(String requestUrl){
    	String result=null;
    	
    	URL url = null;
    	InputStream is;
    	try {
    		for (int i = 0; i < 1; i++) {
    			url = new URL(requestUrl);
    			byte bytes[] = new byte[1024 * 10000];
    			int index = 0;
    			is = url.openStream();
    			int count = is.read(bytes, index,1024 * 100);
    			while (count != -1) {
    				index += count;
    				count = is.read(bytes, index,1);
    			}
    			ByteArrayInputStream biArrayInputStream=new ByteArrayInputStream(bytes);
    			result=uncompress(biArrayInputStream,this.encode);
    		}
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	return result;
    }
    
    
    public static class TrustAnyTrustManager implements TrustManager,X509TrustManager {
    	public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }
    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    
    
	/**
	 * 通过curl命令的方式获取网页源码 ， 支持提前设置 setTimeout
	 * @param url 传入如 https://xxxx.com/ab.html
	 * @return result == success 则成功，info返回源码
	 * @throws IOException 
	 */
	public BaseVO getPageResourceByCurl(String url) throws IOException {
		BaseVO codeVO = getHttpCodeByCurl(url);
		if(codeVO.getResult() - BaseVO.FAILURE == 0) {
			return codeVO;
		}
		if(!codeVO.getInfo().equals("200")) {
			return BaseVO.failure("http response code is "+codeVO.getInfo());
		}
		
		String command = "curl --max-time "+this.timeout+" "+url;
        String text = "";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine())!= null) {
            text += line+"\n";
        }
        
        return BaseVO.success(text);
    }
	
	/**
	 * get方式获取一个url的响应码，这里只是获取响应码，并不获取响应内容。
	 * 支持提前设置 setTimeout
	 * @param url 绝对网址
	 * @return result == success 则成功，info返回响应码
	 */
	public BaseVO getHttpCodeByCurl(String url) {
		int code = -1;
		
		try {
            // 使用 ProcessBuilder 执行 curl 命令并获取响应码
            ProcessBuilder pb = new ProcessBuilder("curl", "-I", "-o", "/dev/null","--max-time",this.timeout+"", "-s", "-w", "%{http_code}", url);
            Process process = pb.start();

            // 读取输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String responseCode = reader.readLine();
            
            code = stringToInt(responseCode, -1);
            
            process.waitFor();
            reader.close();
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return BaseVO.failure(e.getMessage());
        }
		
		if(code > -1) {
			return BaseVO.success(code+"");
		}else {
			return BaseVO.failure("http code gain failure");
		}
	}
	
	/**
	 * 字符型转换为整数型
	 * @param param 待转换的字符串
	 * @param defaultValue 异常后的返回值，默认值
	 * @return 整数
	 */
	private static int stringToInt(String param,int defaultValue){
		int xnx3_result=0;
		
		//首先判断字符串不能为空
		if(param==null||param.equalsIgnoreCase("null")){
			xnx3_result=defaultValue;
		}else{
			try {
				xnx3_result=Integer.parseInt(param);
			} catch (Exception e) {
				xnx3_result=defaultValue;
			}
		}
		
		return xnx3_result;
	}

	/**
	 * 解压缩，也就是
	 * @param gzipData
	 * @return
	 * @throws IOException
	 */
    public static byte[] unbr(byte[] gzipData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gzipData);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = gzipInputStream.read(buffer))!= -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        gzipInputStream.close();
        byteArrayInputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}