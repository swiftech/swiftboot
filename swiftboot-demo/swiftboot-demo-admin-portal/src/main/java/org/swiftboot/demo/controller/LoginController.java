package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.response.Response;

/**
 *
 * @author swiftech
 */
@Tag(name = "Login")
@Controller
@RequestMapping("/")
@ResponseBody
public class LoginController {

    @Operation(description = "登录页面")
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public Response<Void> loginPage() {
        Response<Void> resp = new Response<>();
        resp.setMessage("Implement a URL page for user login if you see this message");
        return resp;
    }
}
