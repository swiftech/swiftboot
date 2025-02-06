package org.swiftboot.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;

import java.io.IOException;

import static org.swiftboot.web.constant.HttpConstants.DEFAULT_RESPONSE_DATA_TYPE;

public abstract class BaseAuthFilter extends OncePerRequestFilter {

    /**
     *
     * @param response
     * @param errorCode
     * @throws IOException
     */
    protected void responseWithError(HttpServletResponse response, String errorCode) throws IOException {
        HttpResponse<?> resp = new HttpResponse<>(errorCode);
        resp.setMsg(ErrorCodeSupport.getErrorMessage(errorCode));
        response.setCharacterEncoding("utf-8");
        response.setContentType(DEFAULT_RESPONSE_DATA_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(resp));
        response.flushBuffer();
        response.getWriter().close();
    }

//    private void responseWithHttpStatus(HttpServletResponse response) throws IOException {
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        response.flushBuffer();
//        response.getWriter().close();
//    }
}
