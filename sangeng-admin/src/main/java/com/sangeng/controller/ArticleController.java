package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.UpdateArticleDto;
import com.sangeng.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/2/10
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    /**
    * 添加博文
    * @param articleDto
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    /**
    * 分页查询文章列表
    * @param pageNum 页码
    * @param pageSize 每页条数
    * @param title 文章标题
    * @param summary 文章摘要
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.listPageArticle(pageNum,pageSize,title,summary);
    }

    /**
    *查询文章详情接口
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/{id}")
    public ResponseResult getArticleInfo(@PathVariable("id") Long id){
        return articleService.getArticleInfo(id);
    }

    /**
    *更新文章接口
    * @param updateArticleDto
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        return articleService.updateArticle(updateArticleDto);
    }

    /**
    * 删除文章
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @DeleteMapping("/{id}")
    public ResponseResult removeArticle(@PathVariable("id") Long id){
        return articleService.removeArticle(id);
    }
}
