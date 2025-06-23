package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.ErrorCode;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

@Tag(name = "Endpoints that give exceptions")
@Controller
@RequestMapping("/exception")
@ResponseBody
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @Operation(description = "test HttpMessageConversionException")
    @GetMapping(value = "http/message/conversion")
    public ResponseEntity<String> httpMessageConversionException() {
        log.info("Testing exception: HttpMessageConversionException");
        throw new HttpMessageConversionException("Testing Exception");
    }

    @Operation(description = "test ErrMessageException")
    @GetMapping(value = "err/message")
    public ResponseEntity<String> errMessageException() {
        log.info("Testing exception: ErrMessageException");
        throw new ErrMessageException(ResponseCode.CODE_SYS_ERR);
    }

    @Operation(description = "test ErrMessageException with customized error code")
    @GetMapping(value = "err/message/custom/code")
    public ResponseEntity<String> errMessageCustomizedCodeException() {
        log.info("Testing ErrMessageException with customized error code");
        throw new ErrMessageException(ErrorCode.CODE_CUSTOMIZED_ERROR);
    }

    @Operation(description = "test ErrMessageException with customized error code and params")
    @GetMapping(value = "err/message/custom/code/params")
    public ResponseEntity<String> errMessageCustomizedCodeParamsException() {
        log.info("Testing ErrMessageException with customized error code and params");
        throw new ErrMessageException(ErrorCode.CODE_CUSTOMIZED_ERROR_WITH_PARAMS, new String[]{"param1", "param2"});
    }
}
