package com.sangeng.service;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;

/**
 * @author Summer
 */
public interface BlogLoginService {
    /**
     * 登录
     * @param user
     * @return
     */
    ResponseResult login(User user);
    /**
    * 用户退出登录
    * @param
    * @return com.sangeng.domain.ResponseResult
    */
    ResponseResult logout();
}
