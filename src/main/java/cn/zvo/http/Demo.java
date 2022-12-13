package cn.zvo.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Demo {
	
	public static void main(String[] args) throws IOException {
		get();
		
		
	}
	
	public static void get() throws IOException {
		Response response =  new Http().get("http://www.guanleiming.com");
		if(response.getCode() == 200) {
			System.out.println(response.getContent());
		}
	}
	
	public static void post() throws IOException {
		Response response = new Http().post("http://www.guanleiming.com");
		if(response.getCode() == 200) {
			System.out.println(response.getContent());
		}
	}
	
	public static void put() throws IOException {
		String url = ""; //请求url
		Map<String, String> headers = new HashMap<String, String>(); //请求头
		String payload = ""; //提交的 payload 数据内容
		Response response = new Http().put(url, headers, payload);
		if(response.getCode() == 200) {
			System.out.println(response.getContent());
		}
	}
}
