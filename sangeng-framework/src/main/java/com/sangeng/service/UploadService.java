package com.sangeng.service;

import com.sangeng.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ljy
 * @date 2023/2/6
 */
public interface UploadService {
    /**
     * 上传文件
     * @param img png,jpg文件
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult uploadImg(MultipartFile img);

}
