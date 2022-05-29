package org.swiftboot.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author swiftech
 */
@Controller
@RequestMapping("/unit-test")
public class MockShiroController {



    @RequestMapping("/signin")
    public @ResponseBody
    String signin(String loginName, String loginPwd) {
        System.out.println("/signin");
        Subject currentUser = SecurityUtils.getSubject();
        Assertions.assertDoesNotThrow(() -> currentUser.login(new UsernamePasswordToken(loginName, loginPwd, "test-auth")));
        return "Hello, World";
    }

    @RequestMapping("/admin_only")
    public @ResponseBody
    String adminOnly() {
        System.out.println("/admin_only");
        return "hello, admin";
    }

    @RequestMapping("/staff_only")
    public @ResponseBody
    String staffOnly() {
        System.out.println("/staff_only");
        return "hello, staff";
    }

    @RequestMapping("/greeting")
    public @ResponseBody
    String greeting() {
        System.out.println("/greeting");
        return "Hello, Shiro";
    }
}
