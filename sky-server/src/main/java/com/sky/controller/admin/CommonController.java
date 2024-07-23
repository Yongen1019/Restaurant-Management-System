package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * common api
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api("Common Controller")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * upload file to Aliyun OSS
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("upload file: {}", file);

        try {
            // create new file name
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            String uploadedFilePath = aliOssUtil.upload(file.getBytes(), filename);

            return Result.success(uploadedFilePath);
        } catch (IOException e) {
            log.error("file upload failed: {}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
