package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.result.UserInfoResult;
import org.swiftboot.web.result.HttpResponse;

/**
 * @author allen
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
        ui.setUserName("fake");
        return new HttpResponse<>(ui);
    }
}
