package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.service.ArticleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/1/30
 */
@RestController
@RequestMapping("/article")
@Api(tags = "文章相关接口")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
    * 查询热门文章
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        //查询热门文章 封装成ResponseResult返回
        return articleService.hotArticleList();
    }

    /**
    * 分页查询文章列表
    * @param pageNum 页码
    * @param pageSize 每页条数
    * @param categoryId 文章id
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    /**
    * 查询文章详情
    * @param id 文章id
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
    * 更新文章浏览量
    * @param id 文章id
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
