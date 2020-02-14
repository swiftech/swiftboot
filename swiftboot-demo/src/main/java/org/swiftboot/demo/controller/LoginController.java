package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.result.HttpResponse;

/**
 *
 * @author swiftech
 */
@Api(tags = {"Login"})
@Controller
@RequestMapping("/")
@ResponseBody
public class LoginController {

    @ApiOperation(notes = "登录页面", value = "登录页面")
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public HttpResponse<Void> adminUserQuery() {
        HttpResponse<Void> resp = new HttpResponse<>();
        resp.setMsg("Implement a URL page for user login if you see this message");
        return resp;
    }
}
