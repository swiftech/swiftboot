package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.dto.PayloadDto;
import org.swiftboot.web.result.HttpResponse;

/**
 * Secure realm that needs user is authenticated and permitted to visit.
 * Require permission by user role.
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
    @Operation(description = "This endpoint requires admin role")
    @GetMapping(value = "/require/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public HttpResponse<PayloadDto> requireAdmin() {
        PayloadDto result = new PayloadDto();
        result.setData("this endpoint is only authorized to admin");
        return new HttpResponse<>(result);
    }

    /**
     * Endpoint requires manage role.
     *
     * @return
     */
    @Operation(description = "This endpoint requires manager role")
    @GetMapping(value = "/require/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public HttpResponse<PayloadDto> requireManager() {
        PayloadDto result = new PayloadDto();
        result.setData("this endpoint is only authorized to manager");
        return new HttpResponse<>(result);
    }
}
