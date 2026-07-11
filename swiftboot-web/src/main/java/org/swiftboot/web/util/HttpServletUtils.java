package org.swiftboot.web.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * HttpServlet 工具类
 *
 * @author swiftech
 **/
public class HttpServletUtils {

    /**
     * 通过 Http Servlet Request 对象获取 Web 应用的 Context Path
     *
     * @param request
     * @param path
     * @return
     */
    public static String makeContextPath(HttpServletRequest request, String path) {
        return request.getContextPath() + "/" + path;
    }

    /**
     * 尝试获取请求客户端中的IP，如果存在反向代理，会尽可能从请求头中获取真实IP。
     * 支持：带有 X-Real-IP 或者 x-forwarded-for 或者 Proxy-Client-IP 或者 WL-Proxy-Client-IP 头的反向代理
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for"); // Reversed Proxy

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(ip)) {
            ip = StringUtils.substringBefore(ip, ",").trim();
        }
        return ip;
    }

//    /**
//     * Convert parameters from servlet request to a map that has multi values connected with comma ','
//     *
//     * @param servletRequest
//     * @return
//     */
//    public static Map<String, String> convertParameters(HttpServletRequest servletRequest) {
//        Map<String, String[]> params = servletRequest.getParameterMap();
//        Map<String, String> ret = new HashMap<>();
//        params.forEach((k, v) -> {
//            ret.put(k, StringUtils.join(v, ","));
//        });
//        return ret;
//    }
}
