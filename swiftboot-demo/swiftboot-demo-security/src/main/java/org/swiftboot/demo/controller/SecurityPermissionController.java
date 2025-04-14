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
 * Test with user permission codes.
 */
@Tag(name = "Security Permission", description = "Security endpoints for testing permissions.")
@Controller
@RequestMapping("/security/api")
public class SecurityPermissionController {

    /**
     * Endpoint requires permission A.
     *
     * @return
     */
    @GetMapping(value = "require/perm_a")
    @PreAuthorize("hasAuthority('PERM_A')")
    @ResponseBody
    public HttpResponse<SomeResult> onePermissionA() {
        SomeResult result = new SomeResult();
        result.setData("this endpoint is only authorized to whom has PERM_A permission");
        return new HttpResponse<>(result);
    }

    /**
     * Endpoint requires permission B.
     *
     * @return
     */
    @GetMapping(value = "require/perm_b")
    @PreAuthorize("hasAuthority('PERM_B')")
    @ResponseBody
    public HttpResponse<SomeResult> onePermissionB() {
        SomeResult result = new SomeResult();
        result.setData("this endpoint is only authorized to whom has PERM_B permission");
        return new HttpResponse<>(result);
    }

    /**
     * Endpoint requires permission A and B.
     *
     * @return
     */
    @GetMapping(value = "require/permissions/or")
    @PreAuthorize("hasAuthority('PERM_A') or hasAuthority('PERM_B')")
    @ResponseBody
    public HttpResponse<SomeResult> permissionsOr() {
        SomeResult result = new SomeResult();
        result.setData("this endpoint is only authorized to whom has either permissions");
        return new HttpResponse<>(result);
    }

    /**
     * Endpoint requires permission A and B.
     *
     * @return
     */
    @GetMapping(value = "require/permissions/and")
    @PreAuthorize("hasAuthority('PERM_A') and hasAuthority('PERM_B')")
    @ResponseBody
    public HttpResponse<SomeResult> permissionsAnd() {
        SomeResult result = new SomeResult();
        result.setData("this endpoint is only authorized to whom has both permissions");
        return new HttpResponse<>(result);
    }

}
