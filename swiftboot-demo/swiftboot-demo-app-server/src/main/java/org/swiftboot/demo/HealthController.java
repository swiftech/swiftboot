package org.swiftboot.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.result.HttpResponse;

@Api(tags = {"Health"})
@Controller
@RequestMapping("/health")
@ResponseBody
public class HealthController {

    @ApiOperation(notes = "App user sign in", value = "App user sign in")
    @GetMapping(value = "")
    public HttpResponse<String> appUserSign() {
        return new HttpResponse<>("OK");
    }
}
