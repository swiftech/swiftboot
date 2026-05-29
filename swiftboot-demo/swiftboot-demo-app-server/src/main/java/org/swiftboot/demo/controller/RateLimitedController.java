package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.annotation.RateLimit;
import org.swiftboot.web.constant.LimitType;
import org.swiftboot.web.response.Response;

@Tag(name = "Rate limit demo")
@Controller
@RequestMapping("/rate_limit")
@ResponseBody
public class RateLimitedController {


    @Operation(description = "Rate limit for user by annotation")
    @GetMapping(value = "user/anno")
    @RateLimit(time = 5000, count = 1, limitType = LimitType.USER)
    public Response<String> userRateLimitByAnno() {
        return Response.builder(String.class).ok().data("Test rate limit for user in 5 seconds").build();
    }

    @Operation(description = "Rate limit for all visitors by annotation")
    @GetMapping(value = "global/anno")
    @RateLimit(time = 2000, count = 2)
    public Response<String> globalRateLimitByAnno() {
        return Response.builder(String.class).ok().data("Test rate limit for all visitors 2 times in 2 seconds").build();
    }

    @Operation(description = "Rate limit for user by configuration")
    @GetMapping(value = "user/cfg")
    public Response<String> userRateLimitByConfig() {
        return Response.builder(String.class).ok().data("Test rate limit for user").build();
    }

    @Operation(description = "Rate limit for all visitors by configuration")
    @GetMapping(value = "global/cfg")
    public Response<String> globalRateLimitByConfig() {
        return Response.builder(String.class).ok().data("Test rate limit for all visitors").build();
    }

}
