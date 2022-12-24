# Java 的 HTTP/HTTPS 请求

* 适配版本：Java8+
* 体积：14KB，无三方依赖，足够精简。
* 协议：自动适配 http、https 协议的请求


## 快速使用
#### 1. pom.xml 中加入：

````
<!-- http/https请求工具 https://github.com/xnx3/http.java -->
<dependency> 
	<groupId>cn.zvo.http</groupId>
	<artifactId>http</artifactId>
	<version>1.0</version>
</dependency>
````

#### 2. Java代码

##### GET 请求

````
Response response = new Http().get("http://www.guanleiming.com");
System.out.println(response);
````

##### POST 请求

````
Response response = new Http().post("https://www.baidu.com");
System.out.println(response);
````


##### POST - 发送 payload 请求

````
Response response = new Http().post("https://www.baidu.com", "payload content", null);
System.out.println(response);
````

##### 自定义请求头 headers 

````
Map<String, String> headers = new HashMap<String, String>(); //请求头
headers.put("Content-Type", "application/x-www-form-urlencoded");
headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");

Response response = new Http().post("https://www.baidu.com", "payload content", headers);
System.out.println(response);
````

##### put 请求，payload 提交数据

````
String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=123"; //请求url
String payload = "payload content"; //提交的 payload 数据内容
Response response = new Http().put(url, payload, null);
System.out.println(response.getContent());
````

##### 请求时自动携带cookies

````
Http http = new Http();
http.setCookies("iwSID=0878b2a7-fb1d-44f7-ac58-003e9a68eda7");

Response response =  http.get("http://www.guanleiming.com");
System.out.println(response);
````


