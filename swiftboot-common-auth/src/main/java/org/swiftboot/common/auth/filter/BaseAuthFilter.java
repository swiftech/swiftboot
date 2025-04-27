package org.swiftboot.common.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.response.ResponseCode;
import org.swiftboot.web.response.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.swiftboot.web.constant.HttpConstants.DEFAULT_RESPONSE_DATA_TYPE;

/**
 * @since 3.0
 */
public abstract class BaseAuthFilter extends OncePerRequestFilter {

    /**
     * @param response
     * @param errorCode
     * @throws IOException
     */
    protected void responseWithError(HttpServletResponse response, String errorCode) throws IOException {
        Response<?> resp = new Response<>(errorCode);
        resp.setMsg(ResponseCode.getErrorMessage(errorCode));
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(DEFAULT_RESPONSE_DATA_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(resp));
        response.flushBuffer();
        response.getWriter().close();
    }

    /**
     *
     * @param response
     * @param statusCode
     * @param msg
     * @throws IOException
     */
    protected void responseWithHttpStatus(HttpServletResponse response, int statusCode, String msg) throws IOException {
        response.setStatus(statusCode);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(DEFAULT_RESPONSE_DATA_TYPE);
        try (PrintWriter writer = response.getWriter()) {
            Response<String> resp = new Response<>(String.valueOf(statusCode), msg);
            String body = JsonUtils.object2Json(resp);
            writer.write(body);
            writer.flush();
        } catch (IOException e) {
            throw e;
        }
    }

}
