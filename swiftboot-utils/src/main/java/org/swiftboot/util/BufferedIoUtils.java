package org.swiftboot.util;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;

/**
 * 缓存的 IO 工具类，可以处理任何的输入流
 *
 * @author swiftech
 **/
public class BufferedIoUtils {

    /**
     * 读取输入流数据到缓存并通过回调返回给调用者
     *
     * @param in
     * @param bufSize  缓存大小 256字节 ~ 10K字节
     * @param callback 读取的字节数组，最大数量为 bufSize
     * @throws Exception
     */
    public static void readInputStream(InputStream in, int bufSize, Callback<byte[]> callback) throws Exception {
        int properBufferSize = properBufferSize(bufSize);
        byte[] buf = new byte[properBufferSize];
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(in);
            int read;
            while ((read = bin.read(buf)) != -1) {
                System.out.println(read);
                callback.on(ArrayUtils.subarray(buf, 0, read));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将输入流的数据写入文件中
     *
     * @param in
     * @param bufSize    缓存大小 256字节 ~ 10K字节
     * @param targetFile
     */
    public static void writeInputStreamToFile(InputStream in, int bufSize, File targetFile) {
        int properBufferSize = properBufferSize(bufSize);
        byte[] buf = new byte[properBufferSize];
        BufferedInputStream bin = null;
        FileOutputStream fout = null;
        try {
            bin = new BufferedInputStream(in);
            fout = new FileOutputStream(targetFile);
            int read;
            while ((read = bin.read(buf)) != -1) {
                System.out.println(read);
                fout.write(buf, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int properBufferSize(int bufSize) {
        if (bufSize < 256) bufSize = 256;
        if (bufSize > 1024 * 10) bufSize = 1024 * 10;
        return bufSize;
    }

    /**
     * Generic Callback
     *
     * @author swiftech
     **/
    public interface Callback<T> {

        /**
         * 返回读取到的数据给调用端处理，发生异常则数据读取中止
         *
         * @param t
         * @throws IOException
         */
        void on(T t) throws IOException;
    }
}
