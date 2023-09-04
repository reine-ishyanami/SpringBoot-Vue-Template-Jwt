package com.reine.backend.controller;

import com.reine.backend.utils.FileHttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author reine
 */
@RestController
@Tag(name = "测试接口")
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final FileHttpUtils fileHttpUtils;

    @Value("${spring.application.name}")
    private String project;

    @Value("${image-host.baseUrl}")
    private String baseUrl;

    @Operation(summary = "测试Security拦截器")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }


    @Operation(summary = "文件上传测试")
    @PostMapping("/upload")
    public String uploadTest(@RequestPart("imgFile") MultipartFile imgFile) {
        return fileHttpUtils.upload(imgFile);
    }

    @Operation(summary = "删除文件测试")
    @DeleteMapping("/{name}")
    public void deleteTest(@PathVariable String name) {
        String url = String.format("%s%s/%s", baseUrl, project, name);
        fileHttpUtils.removeImageFromWebStorage(url);
    }

    @Operation(summary = "查询文件列表")
    @GetMapping("/list")
    public List<?> listImageTest() {
        return fileHttpUtils.getImageListOfProject();
    }

}
