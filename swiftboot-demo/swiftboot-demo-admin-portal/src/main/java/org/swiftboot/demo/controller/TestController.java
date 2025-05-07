package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.exception.ErrorCode;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseBuilder;
import org.swiftboot.web.response.ResponseCode;

/**
 * @author swiftech
 **/
@Tag(name = "Test测试")
@Controller
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Operation(description = "测试资源")
    @RequestMapping(value = "message/resource/code", method = RequestMethod.GET)
    public
    @ResponseBody
    Response<Void> testMessageResource() {
        throw new ErrMessageException(ResponseCode.CODE_SYS_ERR);
    }

    @Operation(description = "测试资源")
    @RequestMapping(value = "message/resource/customize", method = RequestMethod.GET)
    public
    @ResponseBody
    Response<Void> testMessageResourceCustomize() {
        throw new ErrMessageException(ResponseCode.CODE_SYS_ERR, "自定义错误消息");
    }

    @Operation(description = "测试资源")
    @RequestMapping(value = "message/resource/params", method = RequestMethod.GET)
    public
    @ResponseBody
    Response<Void> testMessageResourceParams() {
        return new ResponseBuilder<Void>().code(ErrorCode.CODE_TEST_PARAMS).messageArgs("foo", "bar").build();
    }

    @Operation(description = "测试资源")
    @RequestMapping(value = "message/resource/customize/params", method = RequestMethod.GET)
    public
    @ResponseBody
    Response<Void> testMessageResourceCustomizeParams() {
        return new ResponseBuilder<Void>().code(ErrorCode.CODE_TEST_PARAMS)
                .message("自定义错误消息：{0}和{1}").messageArgs("foo", "bar").build();
    }
}
