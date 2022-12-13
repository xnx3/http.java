package cn.zvo.http;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 响应对象
 * @author 管雷鸣
 */
public class Response {
 
	public String urlString;
	public int defaultPort;
	public String file;
	public String host;
	public String path;
	public int port;
	public String protocol;
	public String query;
	public String ref;
	public String userInfo;
	public String contentEncoding;
	public String content;
	public String contentType;
	public int code;
	public String message;
	public String method;
	public int connectTimeout;
	public int readTimeout;
	public String cookie;
	public Map<String, List<String>> headerFields;
 
	public Vector<String> contentCollection;
 
	public String getContent() {
		return content;
	}
 
	public String getCookie() {
		return cookie;
	}

	public String getContentType() {
		return contentType;
	}
 
	public int getCode() {
		return code;
	}
 
	public String getMessage() {
		return message;
	}
 
	public Vector<String> getContentCollection() {
		return contentCollection;
	}
 
	public String getContentEncoding() {
		return contentEncoding;
	}
 
	public String getMethod() {
		return method;
	}
 
	public int getConnectTimeout() {
		return connectTimeout;
	}
 
	public int getReadTimeout() {
		return readTimeout;
	}
 
	public String getUrlString() {
		return urlString;
	}
 
	public int getDefaultPort() {
		return defaultPort;
	}
 
	public String getFile() {
		return file;
	}
 
	public String getHost() {
		return host;
	}
 
	public String getPath() {
		return path;
	}
 
	public int getPort() {
		return port;
	}
 
	public String getProtocol() {
		return protocol;
	}
 
	public String getQuery() {
		return query;
	}
 
	public String getRef() {
		return ref;
	}
 
	public String getUserInfo() {
		return userInfo;
	}

	public Map<String, List<String>> getHeaderFields() {
		return headerFields;
	}

	@Override
	public String toString() {
		return "HttpResponse [urlString=" + urlString + ", defaultPort="
				+ defaultPort + ", file=" + file + ", host=" + host + ", path="
				+ path + ", port=" + port + ", protocol=" + protocol
				+ ", query=" + query + ", ref=" + ref + ", userInfo="
				+ userInfo + ", contentEncoding=" + contentEncoding
				+ ", content=" + content + ", contentType=" + contentType
				+ ", code=" + code + ", message=" + message + ", method="
				+ method + ", connectTimeout=" + connectTimeout
				+ ", readTimeout=" + readTimeout + ", cookie=" + cookie
				+ ", headerFields=" + headerFields + ", contentCollection="
				+ contentCollection + "]";
	}
	
}
