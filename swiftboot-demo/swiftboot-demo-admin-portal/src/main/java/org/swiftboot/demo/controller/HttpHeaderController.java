package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.request.HttpHeaderRequest;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.validate.ConvertValidateResult;

@Tag(name = "Http header 处理演示")
@Controller
@RequestMapping("/http/header")
public class HttpHeaderController {

    private static final Logger log = LoggerFactory.getLogger(HttpHeaderController.class);

    @Operation(description = "自动获取 http header 值")
    @PostMapping(value = "post")
    @ConvertValidateResult
    public
    @ResponseBody
    Response<Void> httpHeaderPost(
            @RequestBody @Validated @Parameter(description = "会话") HttpHeaderRequest request) {
        log.info("> /http/header/post");
        log.debug(JsonUtils.object2PrettyJson(request));
        return new Response<>();
    }
}
