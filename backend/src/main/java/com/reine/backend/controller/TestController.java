package com.reine.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author reine
 */
@RestController
@Tag(name = "测试接口")
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "测试Security拦截器")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
