package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/2/2
 */
@RestController
@RequestMapping("/category")
@Api(tags = "分类相关接口")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
    * 查询类别列表
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
       return categoryService.getCategoryList();
    }

}
