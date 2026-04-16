package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.request.MyRequest;
import org.swiftboot.web.i18n.MessageHelper;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseCode;
import org.swiftboot.web.validate.ConvertValidateResult;

import java.util.Locale;

@Tag(name = "Message")
@Controller
@RequestMapping("/message")
@ResponseBody
@Validated
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Resource
    @Qualifier("swiftbootAuthMessageSource")
    private MessageSource swiftbootAuthMessageSource;

    @Resource
    private MessageHelper messageHelper;

    @Operation(description = "locale from request")
    @GetMapping(value = "/locale")
    public Response<Void> locale(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                 Locale locale) {
        log.debug("Accept-Language: {}", acceptLanguage);
        log.debug("Locale from request: %s".formatted(locale));
        return Response.builder().code(ResponseCode.CODE_ARGUMENTS_ERROR).build();
    }

    @Operation(description = "locale from request")
    @GetMapping(value = "/locale/anno")
    public Response<Void> localeAnno(Locale locale) {
        log.debug("Language code: {}", locale.toString());
        log.debug("Locale from request: %s".formatted(locale));
        return Response.builder().code(ResponseCode.CODE_ARGUMENTS_ERROR).build();
    }

    @Operation(description = "user message")
    @GetMapping(value = "/user")
    public Response<Void> userProvided() {
        return Response.builder().code(ResponseCode.CODE_SYS_ERR).message("This s an user provided message content").build();
    }

    @Operation(description = "multi language message by response code")
    @GetMapping(value = "/normal")
    public Response<Void> normal() {
        log.debug("Locale from request: %s".formatted(LocaleContextHolder.getLocale()));
        return Response.builder().code(ResponseCode.CODE_ARGUMENTS_ERROR).build();
    }

    @Operation(description = "parameterized and multi language message by response code")
    @GetMapping(value = "/parameterized/code")
    public Response<Void> parameterizedResponseMessage2() {
        return Response.builder().code(ResponseCode.CODE_ARGUMENTS_ERROR_PARAM).messageArgs("param").build();
    }

    @Operation(description = "parameterized and multi language message by helper")
    @GetMapping(value = "/parameterized/helper")
    public Response<Void> parameterizedResponseMessage() {
        log.debug(LocaleContextHolder.getLocale().toString());
        String message = messageHelper.getMessage("i18n.parameterized", "param");
        return Response.builder().message(message).build();
    }

    @Operation(description = "i18n")
    @GetMapping(value = "/i18n")
    public Response<String> i18n() {
        String msgDefault = messageHelper.getMessage("i18n.default");
        System.out.println(msgDefault);
        String msgI18n = messageHelper.getMessage("i18n.test");
        System.out.println(msgI18n);
        return Response.builder(String.class).ok().data(msgI18n).build();
    }

    @GetMapping(value = "/module")
    public Response<String> module() {
        System.out.println(swiftbootAuthMessageSource);
        String moduleMessage = swiftbootAuthMessageSource.getMessage("swifboot.auth.module.name", null, LocaleContextHolder.getLocale());
        return Response.builder(String.class).ok().data(moduleMessage).build();
    }

    @GetMapping(value = "/validation/get")
    @ConvertValidateResult
    public Response<Void> validationGet(
            @Parameter(description = "i8n.validation.my.param")
            @Length(min = 1, max = 2) @RequestParam("myParam") String myParam) {
        return Response.builder().ok().build();
    }

    @GetMapping(value = "/validation/get2")
    @ConvertValidateResult
    public Response<Void> validationGet2(
            @Valid @ModelAttribute MyRequest myRequest, BindingResult bindingResult) {
        return Response.builder().ok().build();
    }

    @PostMapping(value = "/validation/post")
    @ConvertValidateResult
    public Response<Void> validationPost(
            @RequestBody @Validated @Parameter(description = "My Request") MyRequest myRequest, BindingResult bindingResult) {
        return Response.builder().ok().build();
    }
}
