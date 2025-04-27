package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.response.Response;

@Tag(name = "Health")
@Controller
@RequestMapping("/health")
@ResponseBody
public class HealthController {

    @Operation(description = "App user sign in")
    @GetMapping(value = "")
    public Response<String> appUserSign() {
        return new Response<>("OK");
    }
}
