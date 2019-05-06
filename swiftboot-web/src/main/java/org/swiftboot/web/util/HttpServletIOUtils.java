package org.swiftboot.web.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.util.IoUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
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
        OutputStream out = null;
        try {
            String encodedFileName = new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setCharacterEncoding(CharEncoding.UTF_8);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
            byte[] bytes = IoUtils.readAllToBytes(new FileInputStream(file));
            if (bytes == null || bytes.length == 0) {
                throw new RuntimeException("读取文件错误或者文件损坏");
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

    /**
     * 将图片内容写入 HttpServletResponse 流
     *
     * @param file
     * @param response
     */
    public static void writeImageToResponseStream(File file,
                                                  HttpServletResponse response) {
        String fileExt = StringUtils.substringAfterLast(file.getName(), ".");
        if (StringUtils.equalsAnyIgnoreCase(fileExt, "jpg", "png", "gif")) {
            writeFileToResponseStream(file, response, "image/" + fileExt, null);
        }
        else {
            throw new RuntimeException("不是图片格式的文件: " + file.getPath());
        }
    }

    public static void writeFileToResponseStream(File file,
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
            byte[] bytes = IoUtils.readAllToBytes(new FileInputStream(file));
            if (bytes == null || bytes.length == 0) {
                throw new RuntimeException("读取文件错误或者文件损坏");
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
