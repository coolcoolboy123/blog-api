package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.exception.SystemException;
import com.sangeng.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author ljy
 * @date 2023/2/10
 */
@RestController
public class UploadController {

    @Resource
    private UploadService uploadService;


    /**
    * 上传图片接口
    * @param img
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img) {
        return uploadService.uploadImg(img);
    }
}
