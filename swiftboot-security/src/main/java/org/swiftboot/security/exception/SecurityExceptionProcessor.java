package org.swiftboot.security.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.response.Response;

/**
 * Handles Spring Security exception globally.
 * Note that the exceptions from filter can't be caught.
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecurityExceptionProcessor.class);

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class
    })
    @ResponseBody
    public ResponseEntity<?> onDeniedException(AccessDeniedException e) {
        log.debug("onDeniedException...");
        log.error(e.getMessage(), e);
        Response<Void> response = Response.builder().code(String.valueOf(HttpStatus.FORBIDDEN.value())).message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
