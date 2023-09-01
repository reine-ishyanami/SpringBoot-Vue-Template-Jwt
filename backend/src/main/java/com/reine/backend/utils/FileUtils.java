package com.reine.backend.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

/**
 * 文件操作工具类
 *
 * @author reine
 * @since 2022/4/30 15:40
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {

    private final RestTemplate restTemplate;

    @Value("${image-host.baseUrl}")
    private String baseUrl;

    @Value("${spring.application.name}")
    private String project;

    public String upload(MultipartFile file) {
        // 上传地址
        String url = baseUrl + project;
        return formUpload(url, file);
    }

    private String formUpload(String url, MultipartFile file) {
        String filename = getRandomFilename();
        Resource resource = file.getResource();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("imgFile", resource);
        params.add("filename", filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        Response response = restTemplate.postForObject(url, requestEntity, Response.class);
        if (response != null && response.success) {
            Content data = response.data();
            log.debug("{}", data);
            return String.format("%s%s/%s", baseUrl, data.project, data.name);
        } else return "接口请求异常";
    }

    private String getRandomFilename() {
        int length = 16;
        StringBuilder filename = new StringBuilder();
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(str.length());
            filename.append(str.charAt(index));
        }
        return filename.toString();
    }

    private record Response(Boolean success, String message, Content data) {
    }

    private record Content(String project, String name) {
    }
}
