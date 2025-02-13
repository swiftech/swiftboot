package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.constants.PermissionConstants;
import org.swiftboot.demo.result.UserInfoResult;
import org.swiftboot.web.result.HttpResponse;

/**
 * @author swiftech
 */
@Api(tags = {"Security Realm APIs"})
@Controller
@RequestMapping("/security/api")
@ResponseBody
public class SecurityController {

    /**
     * Info of current authenticated user.
     *
     * @return
     */
    @GetMapping(value = "/user/info")
    @ResponseBody
    public HttpResponse<UserInfoResult> userInfo() {
        UserInfoResult ui = new UserInfoResult();
        ui.setUserName("dummy");
        return new HttpResponse<>(ui);
    }

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
