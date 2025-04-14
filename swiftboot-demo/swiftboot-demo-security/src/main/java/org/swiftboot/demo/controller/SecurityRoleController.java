package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.result.SomeResult;
import org.swiftboot.web.result.HttpResponse;

/**
 * Test with user role.
 *
 * @author swiftech
 */
@Tag(name = "Security Role", description = "Security Realm APIs for Admin")
@Controller
@RequestMapping("/security/api")
@ResponseBody
public class SecurityRoleController {

    /**
     * Endpoint requires admin role.
     *
     * @return
     */
    @GetMapping(value = "/require/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public HttpResponse<SomeResult> requireAdmin() {
        SomeResult result = new SomeResult();
        result.setData("this endpoint is only authorized to admin");
        return new HttpResponse<>(result);
    }

    /**
     * Endpoint requires manage role.
     *
     * @return
     */
    @GetMapping(value = "/require/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public HttpResponse<SomeResult> requireManager() {
        SomeResult result = new SomeResult();
        result.setData("this endpoint is only authorized to manager");
        return new HttpResponse<>(result);
    }
}
