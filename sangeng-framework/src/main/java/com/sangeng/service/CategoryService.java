package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-02-02 20:15:25
 */
public interface CategoryService extends IService<Category> {
    /**
    *  查询文章列表
    * @return com.sangeng.domain.ResponseResult
    */
    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listPageCategory(Integer pageNum, Integer pageSize, String name, String status);
}
