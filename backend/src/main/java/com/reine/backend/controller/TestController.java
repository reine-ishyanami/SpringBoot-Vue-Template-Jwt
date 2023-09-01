package com.reine.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reine.backend.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author reine
 */
@RestController
@Tag(name = "测试接口")
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final FileUtils fileUtils;

    @Operation(summary = "测试Security拦截器")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }


    @Operation(summary = "文件上传测试")
    @PostMapping("/upload")
    public String uploadTest(@RequestPart("imgFile") MultipartFile imgFile) {
        return fileUtils.upload(imgFile);
    }

}
