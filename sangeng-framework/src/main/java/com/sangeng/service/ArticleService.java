package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.UpdateArticleDto;
import com.sangeng.domain.entity.Article;

/**
 * @author Summer
 */
public interface ArticleService extends IService<Article> {
    /**
    *  查询热门文章 封装成ResponseResult返回
    * @return ResponseResult
    */
    ResponseResult hotArticleList();
    /**
    * 分页查询文章列表
    * @return com.sangeng.domain.ResponseResult
    */
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);
    /**
     * 查询文章详情
     * @param id
     * @return
     */
    ResponseResult getArticleDetail(Long id);
    /**
     * 更新文章浏览量
     * @param id 文章id
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);
    /**
     * 分页查询文章列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param title 文章标题
     * @param summary 文章摘要
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult listPageArticle(Integer pageNum, Integer pageSize, String title, String summary);
    /**
     *查询文章详情接口
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult getArticleInfo(Long id);
    /**
     *更新文章接口
     * @param updateArticleDto
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult updateArticle(UpdateArticleDto updateArticleDto);

    ResponseResult removeArticle(Long id);
}
