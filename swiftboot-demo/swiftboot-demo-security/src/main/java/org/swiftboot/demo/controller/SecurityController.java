package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.constants.PermissionConstants;
import org.swiftboot.demo.result.UserInfoResult;
import org.swiftboot.web.result.HttpResponse;

/**
 * Secure realm that needs user is authenticated to visit.
 *
 * @author swiftech
 */
@Tag(name = "Security", description = "Security Realm APIs")
@Controller
@RequestMapping("/security/api")
@ResponseBody
public class SecurityController {

    /**
     * Info of current authenticated user.
     *
     * @return
     */
    @Operation(description = "Get user information after login")
    @GetMapping(value = "/user/info")
    @ResponseBody
    public HttpResponse<UserInfoResult> userInfo() {
        UserInfoResult ui = new UserInfoResult();
        ui.setUserName("dummy");
        return new HttpResponse<>(ui);
    }

    @Operation(description = "Get user permissions")
    @GetMapping(value = "/user/permissions")
    @ResponseBody
    public HttpResponse<UserInfoResult> userPermissions() {
        String permissions = StringUtils.join(new String[]{PermissionConstants.PERM_A, PermissionConstants.PERM_B, PermissionConstants.PERM_C}, ",");
        UserInfoResult ui = new UserInfoResult();
        ui.setPermissions(permissions);
        ui.setUserName("dummy");
        return new HttpResponse<>(ui);
    }
}
