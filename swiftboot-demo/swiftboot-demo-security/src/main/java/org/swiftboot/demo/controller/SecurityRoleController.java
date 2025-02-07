package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.result.SomeResult;
import org.swiftboot.web.result.HttpResponse;

/**
 * @author allen
 */
@Api(tags = {"Security Realm APIs for Admin"})
@Controller
@RequestMapping("/security/api")
@ResponseBody
public class SecurityRoleController {

    /**
     *
     * @return
     */
    @GetMapping(value = "/require/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public HttpResponse<SomeResult> requireAdmin() {
        SomeResult result = new SomeResult();
        result.setData("for admin");
        return new HttpResponse<>(result);
    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/require/manager")
//    @PreAuthorize("hasRole('MANAGER')")
    @PreAuthorize("hasAuthority('MANAGER')")
    public HttpResponse<SomeResult> requireManager() {
        SomeResult result = new SomeResult();
        result.setData("for manager");
        return new HttpResponse<>(result);
    }
}
