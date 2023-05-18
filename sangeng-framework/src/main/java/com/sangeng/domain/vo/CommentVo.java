package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author ljy
 * @date 2023/2/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long id;
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 根评论id
     */
    private Long rootId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 所回复的目标评论的userid
     */
    private Long toCommentUserId;
    /**
     * 所回复的目标评论的username
     */
    private String toCommentUserName;
    /**
     * 回复目标评论id
     */
    private Long toCommentId;
    /**
     * 评论的用户名
     */
    private String username;

    private Long createBy;

    private Date createTime;

    private List<CommentVo> children;
}
