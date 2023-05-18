package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/2/6
 */
@RestController
@Api(tags = "更新相关接口")
public class UploadController {

    @Resource
    private UploadService uploadService;

    /**
    * 上传文件
    * @param img png,jpg文件
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
