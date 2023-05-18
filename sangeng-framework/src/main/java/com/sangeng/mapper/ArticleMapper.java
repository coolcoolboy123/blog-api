package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Summer
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
