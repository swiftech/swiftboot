package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.dto.Demo1Dto;
import org.swiftboot.demo.dto.Demo2Dto;
import org.swiftboot.web.response.Response;

@Tag(name = "Health")
@Controller
@RequestMapping("/health")
@ResponseBody
public class HealthController {

    @Operation(description = "Health")
    @GetMapping(value = "")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }


    @Operation(description = "Response Swagger Demo 1")
    @GetMapping(value = "demo1")
    public Response<Demo1Dto> demo1() {
        return Response.builder(Demo1Dto.class).ok().build();
    }

    @Operation(description = "Response Swagger Demo 2")
    @GetMapping(value = "demo2")
    public Response<Demo2Dto> demo2() {
        return Response.builder(Demo2Dto.class).ok().build();
    }

}
