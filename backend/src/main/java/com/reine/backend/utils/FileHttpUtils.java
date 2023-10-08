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

import java.util.*;

/**
 * 文件操作工具类
 *
 * @author reine
 * @since 2022/4/30 15:40
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileHttpUtils {

    private final RestTemplate restTemplate;

    @Value("${image-host.baseUrl}")
    private String baseUrl;

    @Value("${spring.application.name}")
    private String project;

    /**
     * 列出本项目下所有图片
     *
     * @return
     */
    public List<?> getImageListOfProject() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("project", project);
        Response response = restTemplate.getForObject(baseUrl + "list", Response.class, params);
        if (response != null && response.success) {
            if (response.data() instanceof List<?> data) return data;
            else return Collections.emptyList();
        } else return Collections.emptyList();
    }

    /**
     * 删除远程图片
     *
     * @param url
     */
    public void removeImageFromWebStorage(String url) {
        restTemplate.delete(url);
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        // 上传地址
        String url = baseUrl + project;
        return formUpload(url, file);
    }

    private String formUpload(String url, MultipartFile file) {
        String filename = RandomUtils.getRandomStr(16);
        Resource resource = file.getResource();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("imgFile", resource);
        params.add("filename", filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        Response response = restTemplate.postForObject(url, requestEntity, Response.class);
        if (response != null && response.success) {
            if (response.data() instanceof Map<?, ?> data)
                return String.format("%s%s/%s", baseUrl, data.get("project"), data.get("name"));
            else return "接口请求异常";
        } else return "接口请求异常";
    }

    private record Response(Boolean success, String message, Object data) {
    }
}
