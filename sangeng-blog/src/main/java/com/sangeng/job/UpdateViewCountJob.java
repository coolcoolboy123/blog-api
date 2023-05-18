package com.sangeng.job;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Article;
import com.sangeng.service.ArticleService;
import com.sangeng.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ljy
 * @date 2023/2/7
 */
@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    /**
     * 从0秒开始，每10分钟从redis中获取到文章浏览量，更新数据库
     */
    @Scheduled(cron = "0/59 * * * * ? ")
    public void updateViewCount() {
//        1.根据key从redis中获取到含有文章id和浏览量的Map集合
        Map<String, Integer> articles = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);

        List<Article> articleList = articles.entrySet()
                .stream()
                .map(article -> new Article(Long.valueOf(article.getKey()), Long.valueOf(article.getValue())))
                .collect(Collectors.toList());

//        2.更新到数据库
        articleService.updateBatchById(articleList);
    }
}
