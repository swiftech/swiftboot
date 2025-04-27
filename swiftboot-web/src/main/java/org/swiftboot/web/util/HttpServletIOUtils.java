package org.swiftboot.web.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.util.BufferedIoUtils;
import org.swiftboot.web.Info;
import org.swiftboot.util.IoUtils;
import org.swiftboot.web.R;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

import jakarta.servlet.http.HttpServletResponse;

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
        Map<String, String> headers = new HashMap<>() {
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
            throw new RuntimeException(Info.get(HttpServletIOUtils.class, R.REQUIRE_IMAGE_FILE1, file.getPath()));
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
        try (FileInputStream fins = new FileInputStream(file)) {
            writeStreamToResponseStream(fins, response, contentType, headers);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrMessageException(ResponseCode.CODE_SYS_ERR, e.getMessage());
        }
    }

    /**
     * 将输入流写入 HttpServletResponse 流
     *
     * @param inputStream
     * @param response
     * @param contentType
     * @param headers
     * @deprecated 用 writeInputStreamToResponseStream
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
                throw new RuntimeException(Info.get(HttpServletIOUtils.class, R.READ_FROM_INPUT_STREAM_FAIL));
            }
            out = response.getOutputStream();
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrMessageException(ResponseCode.CODE_SYS_ERR, e.getMessage());
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
     * 将输入流写入 HttpServletResponse 流
     *
     * @param inputStream
     * @param response
     * @param contentType
     * @param headers
     */
    public static void writeInputStreamToResponseStream(InputStream inputStream,
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
            out = response.getOutputStream();
            OutputStream finalOut = out;
            BufferedIoUtils.readFrom(inputStream, 1024, b -> {
                try {
                    finalOut.write(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrMessageException(ResponseCode.CODE_SYS_ERR, e.getMessage());
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
