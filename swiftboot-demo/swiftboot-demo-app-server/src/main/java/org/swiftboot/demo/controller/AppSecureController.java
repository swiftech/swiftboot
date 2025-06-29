package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.auth.AuthenticationException;
import org.swiftboot.common.auth.annotation.UserId;
import org.swiftboot.common.auth.annotation.UserName;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.data.model.id.IdGenerator;
import org.swiftboot.demo.dto.AppUserDto;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.demo.service.AppUserService;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseCode;

/**
 * since 3.0.0
 */
@Tag(name = "App Secure Domain")
@Controller
@RequestMapping("/app")
@ResponseBody
public class AppSecureController {

    private static final Logger log = LoggerFactory.getLogger(AppSecureController.class);

    @Resource
    private AppUserService appUserService;

    @Resource
    private IdGenerator<IdPersistable> idGenerator;

    @Operation(description = "Secure domain that needs authentication")
    @GetMapping(value = "secure")
    public Response<String> secure() {
        log.info("> /app/secure");
        AppUserEntity entity = new AppUserEntity();
        // this is for testing the loading of IdGenerator Bean
        String id = idGenerator.generate(entity);
        log.info("generated ID: {}", id);
        return new Response<>("Authenticated");
    }

    @Operation(description = "Test data retrieval with user id in session or token")
    @GetMapping(value = "data")
    public Response<AppUserDto> dataRetrieval(@UserId String userId, @UserName String userName) {
        log.info("> /app/data");
        log.debug("userId: " + userId);
        log.debug("userName: " + userName);
        AppUserDto userInfo = appUserService.getUserInfo(userId);
        if (userInfo != null) {
            log.debug(userInfo.getLoginName());
            return new Response<>(userInfo);
        }
        return new Response<>(ResponseCode.CODE_SYS_ERR, "No user found");
    }

    @Operation(description = "Test unauthorized")
    @GetMapping(value = "unauthorized")
    public Response<Void> unauthorized() {
        log.info("> /app/unauthorized");
        String userRole = "GUEST";
        if (!"admin".equals(userRole)) {
            throw new AuthenticationException("Unauthorized");
        }
        return Response.builder().message("Unauthorized").build();
    }

}
