package org.swiftboot.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.response.Response;

import java.io.IOException;

/**
 * Handler authorization exception globally.
 *
 * @since 3.0
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionProcessor {

    private static final Logger log = LoggerFactory.getLogger(AuthExceptionProcessor.class);

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<?> handle401Exception(AuthenticationException e, HttpServletRequest req) {
        if (log.isDebugEnabled()) log.debug("handle %s to HTTP response ".formatted(e.getClass().getSimpleName()));
        Response<String> response = new Response<>(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getMessage());
        try {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonUtils.object2Json(response));
        } catch (IOException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
    }
}
