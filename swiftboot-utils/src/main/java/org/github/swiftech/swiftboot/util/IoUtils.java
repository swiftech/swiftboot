package org.github.swiftech.swiftboot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author swiftech 2015-02-16
 */
public class IoUtils {

    /**
     * 从输入流中读取所有字节内容（不能用于大数据量）
     * @param ins
     * @return
     */
    public static byte[] readAllToBytes(InputStream ins) {
        try {
            int len = ins.available();
            byte[] data = new byte[len];
            if (ins.read(data) != -1) {
                return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中读取所有字符串内容（不能用于大数据量）
     *
     * @param ins
     * @return
     */
    public static String readAllToString(InputStream ins) {
        return new String(readAllToBytes(ins));
    }

    /**
     * @param ins
     * @return
     * @deprecated
     */
    public static String readAll(InputStream ins) {
        return new String(readAllToBytes(ins));
    }

    /**
     * 从输入流中按行读取所有字符串内容
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static List<String> readToStringList(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> array = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            array.add(line);
        }
        return array;
    }

    /**
     * 从输入流中按行读取所有字符串内容
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String[] readToStringArray(InputStream inputStream) throws IOException {
        return readToStringList(inputStream).toArray(new String[]{});
    }
}
