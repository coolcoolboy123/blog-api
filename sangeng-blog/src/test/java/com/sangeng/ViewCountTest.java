package com.sangeng;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Article;
import com.sangeng.service.ArticleService;
import com.sangeng.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ljy
 * @date 2023/5/15
 */
@SpringBootTest(classes = SanGengBlogApplication.class)
public class ViewCountTest {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    @Test
    public void testViewCount() {
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);

        List<Article> articleList = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        articleService.updateBatchById(articleList);

//        System.out.println(articleList);
    }
}
