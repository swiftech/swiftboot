package org.swiftboot.web.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.util.IoUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.swiftboot.util.constant.FileConstants.IMAGE_FILES;

/**
 * HttpServlet IO 工具类
 *
 * @author swiftech
 **/
public class HttpServletIOUtils {

    /**
     * 将文件内容写入 HttpServletResponse 流
     *
     * @param file
     * @param response
     */
    public static void writeFileToResponseStream(File file, HttpServletResponse response) {
        String encodedFileName = new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put("Content-Disposition", "attachment; filename=" + encodedFileName);
            }
        };
        writeFileToResponseStream(file, response, "application/octet-stream", headers);
    }

    /**
     * 将图片内容写入 HttpServletResponse 流
     *
     * @param file     支持 jpg，png，bmp, webp, gif 五种类型
     * @param response
     */
    public static void writeImageToResponseStream(File file,
                                                  HttpServletResponse response) {
        String fileExt = StringUtils.substringAfterLast(file.getName(), ".");
        if (StringUtils.equalsAnyIgnoreCase(fileExt, IMAGE_FILES)) {
            String contentType = URLConnection.guessContentTypeFromName(file.getName());
            writeFileToResponseStream(file, response, contentType, null);
        }
        else {
            throw new RuntimeException("不是图片格式的文件: " + file.getPath());
        }
    }

    /**
     * 将文件内容写入 HttpServletResponse 流
     *
     * @param file
     * @param response
     * @param contentType
     * @param headers
     */
    public static void writeFileToResponseStream(File file,
                                                 HttpServletResponse response,
                                                 String contentType,
                                                 Map<String, String> headers) {
        try {
            writeStreamToResponseStream(new FileInputStream(file), response, contentType, headers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, e.getMessage());
        }
    }

    /**
     * 将输入流写入 HttpServletResponse 流
     *
     * @param inputStream
     * @param response
     * @param contentType
     * @param headers
     */
    public static void writeStreamToResponseStream(InputStream inputStream,
                                                   HttpServletResponse response,
                                                   String contentType,
                                                   Map<String, String> headers) {
        OutputStream out = null;
        try {
            response.setCharacterEncoding(CharEncoding.UTF_8);
            response.setContentType(contentType);
            if (headers != null) {
                for (String k : headers.keySet()) {
                    String v = headers.get(k);
                    response.setHeader(k, v);
                }
            }
            byte[] bytes = IoUtils.readAllToBytes(inputStream);
            if (bytes == null || bytes.length == 0) {
                throw new RuntimeException("无法从输入流中读取字节");
            }
            out = response.getOutputStream();
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
