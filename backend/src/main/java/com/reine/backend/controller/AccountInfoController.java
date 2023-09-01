package com.reine.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reine.backend.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author reine
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "账号相关接口")
@RequestMapping("/api/account")
public class AccountInfoController {

    private final FileUtils fileUtils;

    @Operation(summary = "上传头像")
    @PostMapping("/avatar")
    public String uploadAvatar(@RequestPart("imgFile") MultipartFile imgFile) {
        return fileUtils.upload(imgFile);
    }

}
