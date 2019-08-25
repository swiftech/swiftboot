package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.command.SessionCommand;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.validate.ConvertValidateResult;

@Api(tags = {"Http header 处理演示"})
@Controller
@RequestMapping("/http/header")
public class HttpHeaderController {

    private Logger log = LoggerFactory.getLogger(HttpHeaderController.class);

    @ApiOperation(notes = "自动获取http header值", value = "自动获取http header值")
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ConvertValidateResult
    public
    @ResponseBody
    HttpResponse<Void> httpHeaderPost(
            @RequestBody @Validated @ApiParam("会话") SessionCommand command) {
        log.info("> /http/header/post");
        log.debug(JsonUtils.object2PrettyJson(command));

        log.info(command.getSessionId());

        return new HttpResponse<>();
    }
}
