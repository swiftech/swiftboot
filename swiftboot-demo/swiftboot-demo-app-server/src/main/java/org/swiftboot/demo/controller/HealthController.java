package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.dto.Demo1Dto;
import org.swiftboot.demo.dto.Demo2Dto;
import org.swiftboot.service.service.CaptchaService;
import org.swiftboot.web.response.Response;

import java.time.LocalDateTime;

@Tag(name = "Health")
@Controller
@RequestMapping("/health")
@ResponseBody
public class HealthController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CaptchaService captchaService;

    @Operation(description = "Health")
    @GetMapping(value = "")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }


    @Operation(description = "Response Swagger Demo 1")
    @GetMapping(value = "demo1")
    public Response<Demo1Dto> demo1() {
        return Response.builder(Demo1Dto.class).ok().build();
    }

    @Operation(description = "Response Swagger Demo 2")
    @GetMapping(value = "demo2")
    public Response<Demo2Dto> demo2() {
        return Response.builder(Demo2Dto.class).ok().build();
    }

    @Operation(description = "Redis Health")
    @GetMapping(value = "redis")
    public Response<String> redis() {
        stringRedisTemplate.opsForHash().put("greeting", "hello", LocalDateTime.now().toString());
        Object greeting = stringRedisTemplate.opsForHash().get("greeting", "hello");
//        stringRedisTemplate.opsForValue().set("greeting", "Hello Redis at " + LocalDateTime.now());
//        String greeting = stringRedisTemplate.opsForValue().get("greeting");
        return Response.builder(String.class).ok().data(String.valueOf(greeting)).build();
    }

    @Operation(description = "Captcha Health")
    @GetMapping(value = "captcha")
    public Response<String> captcha() {
        String captchaId = captchaService.createCaptcha("SB_APP_SERVER_CAPTCHA");
        String captcha = captchaService.getCaptchaText("SB_APP_SERVER_CAPTCHA", captchaId);
        return Response.builder(String.class).ok().data(captcha).build();
    }


}
