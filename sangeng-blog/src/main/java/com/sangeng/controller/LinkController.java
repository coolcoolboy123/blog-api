package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.service.LinkService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/2/3
 */
@RestController
@RequestMapping("/link")
@Api(tags = "友链相关接口")
public class LinkController {

    @Resource
    private LinkService linkService;
    /**
    * 查询所有友链
    * @return ResponseResult
    */
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
