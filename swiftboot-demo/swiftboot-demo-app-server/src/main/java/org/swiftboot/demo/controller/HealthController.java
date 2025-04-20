package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.result.HttpResponse;

@Tag(name = "Health")
@Controller
@RequestMapping("/health")
@ResponseBody
public class HealthController {

    @Operation(description = "App user sign in")
    @GetMapping(value = "")
    public HttpResponse<String> appUserSign() {
        return new HttpResponse<>("OK");
    }
}
