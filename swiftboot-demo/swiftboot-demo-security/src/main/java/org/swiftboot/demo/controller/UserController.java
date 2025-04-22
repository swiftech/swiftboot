package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.common.auth.annotation.UserId;
import org.swiftboot.demo.constants.PermissionConstants;
import org.swiftboot.demo.dto.UserInfoDto;
import org.swiftboot.demo.service.UserService;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;

/**
 * Secure realm that needs user is authenticated to visit.
 *
 * @author swiftech
 */
@Tag(name = "User", description = "User information and permissions")
@Controller
@RequestMapping("/security/api")
@ResponseBody
public class UserController {

    @Resource
    private UserService userService;

    /**
     * Info of current authenticated user.
     *
     * @return
     */
    @Operation(description = "Get user information after login")
    @GetMapping(value = "/user/info")
    @ResponseBody
    public HttpResponse<UserInfoDto> userInfo(@UserId String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new ErrMessageException(ErrorCodeSupport.CODE_NO_SIGNIN, "User does not login");
        }
        UserInfoDto user = userService.findById(userId);
        return new HttpResponse<>(user);
    }

    @Operation(description = "Get user permissions")
    @GetMapping(value = "/user/permissions")
    @ResponseBody
    public HttpResponse<UserInfoDto> userPermissions() {
        String permissions = StringUtils.join(new String[]{PermissionConstants.PERM_A, PermissionConstants.PERM_B, PermissionConstants.PERM_C}, ",");
        UserInfoDto ui = new UserInfoDto();
        ui.setPermissions(permissions);
        ui.setNickName("dummy");
        return new HttpResponse<>(ui);
    }
}
