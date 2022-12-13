package cn.zvo.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException; 
import java.io.InputStream; 
import java.net.HttpURLConnection; 
import java.net.MalformedURLException;
import java.net.URL; 
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.zip.GZIPInputStream;

/**
 * http网络请求
 * @author 管雷鸣
 */ 
public class Http { 
	public final static String UTF8="UTF-8";
	public final static String GBK="GBK";
	
    private String encode; 	//默认编码格式
    private String cookies="";	//每次请求都用自动发送此cookies,请求完毕后自动更新此cookies
    private int timeout = 30000;	//超时时间，默认30秒
    
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
     * @param url 目标URL地址
     * @return 响应对象 若是返回null则相应失败抛出异常
	 * @throws IOException 
     */ 
    public Response get(String url) throws IOException{ 
       	return this.send(url, "GET", null, null);
    } 
    
    /**
     * GET请求
     * @param url 目标URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response get(String url, Map<String, String> params) throws IOException{ 
		return this.send(url, "GET", params, null);
    } 
   
    /**
     * get请求
     * @param url 目标URL地址
     * @param params 参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response get(String url, Map<String, String> params, Map<String, String> propertys) throws IOException{ 
		return this.send(url, "GET", params, propertys);
    } 
   
    /**
     * POST请求
     * @param url 目标URL地址
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response post(String url) throws IOException { 
    	return this.send(url, "POST", null, null);
    } 
   
    /**
     * POST请求
     * @param url 目标URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response post(String url, Map<String, String> params) throws IOException { 
		return this.send(url, "POST", params, null);
    } 
   
    /**
     * POST请求
     * @param url 目标URL地址
     * @param params 参数集合
     * @param headers 请求属性,headers
     * @return 响应对象
     * @throws IOException 
     */ 
    public Response post(String url, Map<String, String> params, Map<String, String> headers) throws IOException{ 
		return this.send(url, "POST", params, headers);
    } 
    
    /**
     * PUT 请求
     * @param url 目标URL地址
     * @param headers
     * @param payload put提交的payload，可传入null则为不提交payload数据
     * @throws IOException 
     */
    public Response put(String url, Map<String, String> headers, String payload) throws IOException {
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("payload", payload);
    	return send(url, "PUT", params, headers);
    }

	/**
	 * 将Map转换为URL的请求GET参数
	 * @param url 目标URL地址，如：http://www.xnx3.com/test.php
	 * @param parameters 请求参数Map集合
	 * @return 完整的GET方式网址
	 */
	public static String mapToUrl(String url,Map<String, String> parameters){
		int i = 0; 
		if(url.indexOf("?") > 0){
			i = 1;
		}
		
		StringBuffer param = new StringBuffer(); 
        
        for (String key : parameters.keySet()) { 
            if (i == 0){
            	param.append("?");
            }else{
            	param.append("&");
            }
            param.append(key).append("=").append(parameters.get(key)); 
            i++; 
        } 
        url += param; 
        return url;
	}
	
	/**
	 * 将Map参数转变为 URL后的字符组合形势。
	 * @param parameters Map 
	 * @return 返回url拼接的key、value，返回如： key=value&amp;key=value
	 */
	public static String mapToQueryString(Map<String,String> parameters){
    	String data = "";
    	StringBuffer param = new StringBuffer(); 
        if(parameters != null){
        	int i = 0; 
            for (Map.Entry<String, String> entry : parameters.entrySet()) {  
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
     * HTTP请求
     * @param url 目标URL地址
     * @param method  GET/POST
     * @param parameters  添加由键值对指定的请求参数
     * @param propertys  添加由键值对指定的一般请求属性,headers
     * @return 响应对象
     * @throws IOException IO异常
     */ 
    public Response send(String url, String method, Map<String, String> parameters, 
            Map<String, String> propertys) throws IOException { 
        HttpURLConnection urlConnection = null; 
   
        if (method.equalsIgnoreCase("GET") && parameters != null) { 
            url = mapToUrl(url, parameters);
        } 
   
        URL requestURL = new URL(url); 
        urlConnection = (HttpURLConnection) requestURL.openConnection(); 
        urlConnection.setRequestMethod(method); 
        urlConnection.setDoOutput(true); 
        urlConnection.setDoInput(true); 
        urlConnection.setUseCaches(false); 
        urlConnection.setRequestProperty("Cookie", this.cookies);
   
        if (propertys != null) 
            for (String key : propertys.keySet()) { 
                urlConnection.addRequestProperty(key, propertys.get(key)); 
            } 
   
        if (method.equalsIgnoreCase("POST") && parameters != null) { 
            StringBuffer param = new StringBuffer(); 
            for (String key : parameters.keySet()) { 
                param.append("&"); 
                param.append(key).append("=").append(parameters.get(key)); 
            } 
            urlConnection.getOutputStream().write(param.toString().getBytes()); 
            urlConnection.getOutputStream().flush(); 
            urlConnection.getOutputStream().close(); 
        }else if(method.equalsIgnoreCase("PUT")) {
        	String payload = parameters.get("payload");
        	System.out.println("payload:"+payload);
        	
        	 if(payload != null && payload.length() > 0) {
                 // 建立输入流，向指向的URL传入参数
                 DataOutputStream dos=new DataOutputStream(urlConnection.getOutputStream());
                 dos.writeBytes(payload);
                 dos.flush();
                 dos.close();
             }
        }
        return this.makeContent(url, urlConnection); 
    } 
    
    /**
     * 得到响应对象
     * @param url 目标URL地址，如 http://xxx.com/a.jsp
     * @param urlConnection {@link HttpURLConnection}
     * @return 响应对象
     * @throws IOException IO异常
     */ 
    public Response makeContent(String url, HttpURLConnection urlConnection) throws IOException { 
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
        	}
            
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            httpResponser.content = result.toString(this.encode);
            
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
            httpResponser.headerFields = urlConnection.getHeaderFields();
        } catch (IOException e) {
        	e.printStackTrace();
        	httpResponser.code = 0;
        	httpResponser.message = e.getMessage();
        } finally { 
            if (urlConnection != null) 
                urlConnection.disconnect(); 
        } 
        return httpResponser; 
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
    
}