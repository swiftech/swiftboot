package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.command.HttpHeaderCommand;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.dto.HttpResponse;
import org.swiftboot.web.validate.ConvertValidateResult;

@Tag(name = "Http header 处理演示"})
@Controller
@RequestMapping("/http/header")
public class HttpHeaderController {

    private static final Logger log = LoggerFactory.getLogger(HttpHeaderController.class);

    @Operation(description = "自动获取 http header 值", value = "自动获取 http header 值")
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ConvertValidateResult
    public
    @ResponseBody
    HttpResponse<Void> httpHeaderPost(
            @RequestBody @Validated @Parameter(description = "会话") HttpHeaderCommand command) {
        log.info("> /http/header/post");
        log.debug(JsonUtils.object2PrettyJson(command));
        return new HttpResponse<>();
    }
}
