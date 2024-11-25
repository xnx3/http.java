package cn.zvo.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * 解压， 用于解压 gzip、br 等数据
 * @author 管雷鸣
 *
 */
public class Unzip {

	/**
	 * 解压缩 gzip 压缩的数据
	 * @param gzipData
	 * @return
	 * @throws IOException
	 */
    public static byte[] unGzip(byte[] gzipData) throws IOException {
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
    
    /**
     * deflate 解压
     * @param deflateData
     * @return
     * @throws IOException
     */
	public static byte[] unDeflate(byte[] deflateData) throws IOException {
	    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(deflateData);
	    InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
	
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	
	    byte[] buffer = new byte[1024];
	    int len;
	    while ((len = inflaterInputStream.read(buffer))!= -1) {
	        byteArrayOutputStream.write(buffer, 0, len);
	    }
	
	    inflaterInputStream.close();
	    byteArrayInputStream.close();
	
	    return byteArrayOutputStream.toByteArray();
	}
    
	//还缺少一个对br解压缩的
}
