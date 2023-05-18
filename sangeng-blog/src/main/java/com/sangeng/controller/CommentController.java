package com.sangeng.controller;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddCommentDto;
import com.sangeng.domain.entity.Comment;
import com.sangeng.service.CommentService;
import com.sangeng.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/2/5
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论相关接口")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
    * 查询评论列表
    * @param articleId 文章id
    * @param pageNum 页码
    * @param pageSize 每页条数
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    /**
    * 添加评论
    * @param comment 实体
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping("addComment")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    /**
    * 查询友链评论列表
    * @param pageNum 页码
    * @param pageSize 每页条数
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表",notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数")
    })
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
