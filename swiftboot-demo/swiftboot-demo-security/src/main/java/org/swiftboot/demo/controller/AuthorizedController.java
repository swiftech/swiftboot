package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.response.Response;

@Tag(name = "Authorized", description = "Authorized based on Spring Security")
@Controller
@RequestMapping("/security/auth")
@ResponseBody
public class AuthorizedController {

    @Operation(description = "authorized endpoint")
    @GetMapping("authorized")
    @ResponseBody
    public Response<String> authorized() {
        return new Response<>("authorized");
    }
}
