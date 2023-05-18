package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ljy
 * @date 2023/1/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleVo {
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 访问量
     */
    private Long viewCount;
}
