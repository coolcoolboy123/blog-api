package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-02-03 15:48:50
 */
public interface LinkService extends IService<Link> {
    /**
     * 查询所有友链
     * @return
     */
    ResponseResult getAllLink();

    ResponseResult listPage(Integer pageNum, Integer pageSize, String name, String status);
}
