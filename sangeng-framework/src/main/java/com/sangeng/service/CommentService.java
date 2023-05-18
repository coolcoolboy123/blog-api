package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-02-05 13:20:56
 */
public interface CommentService extends IService<Comment> {
    /**
     * 查询评论列表
     *
     * @param commentType 文章类型
     * @param articleId 文章id
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);
    /**
     * 添加评论
     * @param comment 实体
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult addComment(Comment comment);
}
