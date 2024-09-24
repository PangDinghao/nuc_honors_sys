package com.nuc.sys.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nuc.sys.common.FilePathHolder;
import com.nuc.sys.utils.FileDownloadUtil;
import com.nuc.sys.utils.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileContoller {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 上传文件
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) {
        try {
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName =  UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            FileUploadUtil.uploadToServer(file, FilePathHolder.getFilePath(), fileName);


            // 构建JSON对象
            ObjectNode responseJson = objectMapper.createObjectNode();
            responseJson.put("url", fileName);

            return ResponseEntity.ok().body(responseJson);
        } catch (Exception e) {
            ObjectNode errorJson = objectMapper.createObjectNode();
            errorJson.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson);
        }
    }

    //下载文件
    @GetMapping("/download")
    public void download( String fileName, HttpServletResponse response) {
        FileDownloadUtil.downloadToBrowser( FilePathHolder.getFilePath() + fileName,fileName,response);
    }
}
