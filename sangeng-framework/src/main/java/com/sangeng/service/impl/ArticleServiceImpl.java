package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.UpdateArticleDto;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.ArticleTag;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.*;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.service.ArticleService;
import com.sangeng.service.ArticleTagService;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ljy
 * @date 2023/1/30
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private CategoryService categoryService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleTagService articleTagService;


    /**
     * 查询热门文章
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询十条
        Page<Article> page = new Page(SystemConstants.ARTICLE_STATUS_DRAFT,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        //封装成HotArticleVo
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        //封装响应返回
        return ResponseResult.okResult(articleVos);
    }


    /**
     * 分页查询文章列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
         //查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
         //如果 有categoryId 就要 查询时和传入的相同
        wrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
         //状态是正式发布的
        wrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
         //对isTop进行降序
        wrapper.orderByDesc(Article::getIsTop);
         //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        //查询categoryName
        List<Article> articles = page.getRecords();
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        //封装成pageVo
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        //封装响应返回
        return ResponseResult.okResult(pageVo);

    }

    /**
    * 查询文章详情
    * @param id 文章id
    * @return com.sangeng.domain.ResponseResult
    */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 更新文章浏览量
     * @param id 文章id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 分页查询文章列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param title 文章标题
     * @param summary 文章摘要
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult listPageArticle(Integer pageNum, Integer pageSize, String title, String summary) {

        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        wrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);

        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page, wrapper);

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     *查询文章详情接口
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult getArticleInfo(Long id) {
        Article article = getById(id);
        //获取关联标签
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> tagList = articleTagService.list(wrapper);
        //把标签id集合转换为long类型并赋值
        List<Long> list = tagList.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());

        //封装vo
        ArticleInfoVo articleInfoVo = BeanCopyUtils.copyBean(article, ArticleInfoVo.class);
        articleInfoVo.setTags(list);
        return ResponseResult.okResult(articleInfoVo);
    }

    /**
     *更新文章接口
     * @param updateArticleDto
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult updateArticle(UpdateArticleDto updateArticleDto) {
        //更新博客信息
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        updateById(article);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(wrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> collect = updateArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(collect);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult removeArticle(Long id) {
        Article article = getById(id);
        removeById(article.getId());
        return ResponseResult.okResult();
    }
}
