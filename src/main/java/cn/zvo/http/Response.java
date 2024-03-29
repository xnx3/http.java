package cn.zvo.http;

import java.io.ByteArrayOutputStream;
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
	public ByteArrayOutputStream outputStream; //v1.3 增加
	
 
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

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public void setDefaultPort(int defaultPort) {
		this.defaultPort = defaultPort;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public void setHeaderFields(Map<String, List<String>> headerFields) {
		this.headerFields = headerFields;
	}

	public void setContentCollection(Vector<String> contentCollection) {
		this.contentCollection = contentCollection;
	}

	public ByteArrayOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ByteArrayOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public String toString() {
		return "Response [urlString=" + urlString + ", defaultPort=" + defaultPort + ", file=" + file + ", host=" + host
				+ ", path=" + path + ", port=" + port + ", protocol=" + protocol + ", query=" + query + ", ref=" + ref
				+ ", userInfo=" + userInfo + ", contentEncoding=" + contentEncoding + ", content=" + content
				+ ", contentType=" + contentType + ", code=" + code + ", message=" + message + ", method=" + method
				+ ", connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout + ", cookie=" + cookie
				+ ", headerFields=" + headerFields + ", contentCollection=" + contentCollection + ", outputStream="
				+ outputStream + "]";
	}

	
}
