package cn.zvo.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * DEMO 示例
 * @author 管雷鸣
 */
public class Demo {
	
	public static void main(String[] args) throws IOException {
		get(); //get 请求
//		post(); //post 请求
//		post_payload(); //post - payload 请求
		//headers(); //自定义请求头 headers 
//		put(); //put 请求，payload 提交数据
		//cookies(); //请求时自动携带cookies
	}
	
	/**
	 * get 请求
	 * @throws IOException
	 */
	public static void get() throws IOException {
		Http http = new Http();
		http.setSslProtocol("TLS");
		Response response = http.get("https://www.szppsa.cn/");
		System.out.println(response);
	}
	
	/**
	 * post 请求
	 * @throws IOException
	 */
	public static void post() throws IOException {
		Response response = new Http().post("https://www.baidu.com");
		System.out.println(response);
	}
	
	/**
	 * post - 发送 payload 请求
	 * @throws IOException
	 */
	public static void post_payload() throws IOException {
		Response response = new Http().post("https://www.baidu.com", "payload content", null);
		System.out.println(response);
	}
	
	/**
	 * 自定义请求头 headers 
	 * @throws IOException
	 */
	public static void headers() throws IOException {
		Map<String, String> headers = new HashMap<String, String>(); //请求头
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
		
		Response response = new Http().post("https://www.baidu.com", "payload content", headers);
		System.out.println(response);
	}
	
	/**
	 * put 请求，payload 提交数据
	 * @throws IOException
	 */
	public static void put() throws IOException {
		String url = "http://xxxx.com"; //请求url
		String payload = "payload content"; //提交的 payload 数据内容
		Response response = new Http().put(url, payload, null);
		System.out.println(response.getContent());
	}
	
	/**
	 * 请求时自动携带cookies
	 * @throws IOException
	 */
	public static void cookies() throws IOException {
		Http http = new Http();
		http.setCookies("iwSID=0878b2a7-fb1d-44f7-ac58-003e9a68eda7");
		
		Response response =  http.get("http://www.guanleiming.com");
		System.out.println(response);
	}
	
}
