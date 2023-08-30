package com.reine.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.activation.MimetypesFileTypeMap;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件操作工具类
 *
 * @author reine
 * @since 2022/4/30 15:40
 */
@Slf4j
public class FileUtils {

    private static final String uploadUrl = "http://localhost:8824/image/";
    private static final String deleteUrl = "http://localhost:8824/image/";
    private static final String downloadUrl = "http://localhost:8824/image/";

    /**
     * 文件上传工具类
     *
     * @return 文件查询地址
     */
    public static String upload(String project, MultipartFile file) throws JsonProcessingException {
        // 上传地址
        String url = uploadUrl + project;
        // 文件
        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("imgFile", file);
        return formUpload(url, fileMap);
    }

    private static String formUpload(String urlStr,
                                     Map<String, MultipartFile> fileMap) throws JsonProcessingException {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String boundary = "---------------------------123821742118716";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // file
            if (fileMap != null) {
                MultipartFile imgFile = fileMap.get("imgFile");
                // 获取初始文件名
                String originalFilename = imgFile.getOriginalFilename();
                if (StringUtils.isBlank(originalFilename)) originalFilename = imgFile.getName();
                // 重命名文件
                int index = originalFilename.lastIndexOf(".");
                String extensionName = originalFilename.substring(index - 1);
                String uuid = UUID.randomUUID().toString();
                String filename = uuid.substring(uuid.lastIndexOf("-") + 1) + extensionName;
                // 根据修改后的文件名创建临时文件
                String name = filename.substring(0, filename.lastIndexOf("."));
                String prefix = filename.substring(filename.lastIndexOf("."));
                File tempFile = File.createTempFile(name, prefix);
                // 写入临时文件
                imgFile.transferTo(tempFile);
                // 获取文件名
                String fileName = tempFile.getName();
                // 没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                String contentType = new MimetypesFileTypeMap().getContentType(tempFile);
                // contentType非空采用filename匹配默认的图片类型
                if (!"".equals(contentType)) {
                    if (fileName.endsWith(".png")) {
                        contentType = "image/png";
                    } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpe")) {
                        contentType = "image/jpeg";
                    } else if (fileName.endsWith(".gif")) {
                        contentType = "image/gif";
                    } else if (fileName.endsWith(".ico")) {
                        contentType = "image/image/x-icon";
                    }
                }
                if (contentType == null || contentType.isEmpty()) contentType = "application/octet-stream";
                StringBuilder strBuf = new StringBuilder();
                strBuf.append("\r\n").append("--").append(boundary)
                        .append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + "imgFile" + "\"; filename=\"")
                        .append(fileName)
                        .append("\"\r\n");
                System.out.println("imgFile" + "," + fileName);
                strBuf.append("Content-Type:").append(contentType).append("\r\n\r\n");
                out.write(strBuf.toString().getBytes());
                DataInputStream in = new DataInputStream(new FileInputStream(tempFile));
                int bytes;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) out.write(bufferOut, 0, bytes);
                in.close();
                deleteFile(tempFile);
            }
            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuilder strBuf = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) strBuf.append(line).append("\n");
            res = strBuf.toString();
            reader.close();
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            log.error(e.getMessage());
        } finally {
            if (conn != null) conn.disconnect();
        }
        Response response = new ObjectMapper().readValue(res, Response.class);
        return String.format("%s%s/%s", downloadUrl, response.data().project, response.data().filename);
    }

    public static String delete(String projectAndFileName) throws Exception {
        HttpURLConnection conn = null;
        try {
            String deleteUrl = FileUtils.deleteUrl + projectAndFileName;
            URL url = new URL(deleteUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 执行删除操作
            conn.getResponseCode();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new Exception("删除失败");
        } finally {
            if (conn != null) conn.disconnect();
        }
        return "删除成功";

    }

    /**
     * 删除临时文件
     *
     * @param file
     */
    private static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    private record Response(Boolean success, String message, Content data) {
    }

    private record Content(String project, String filename) {
    }
}
