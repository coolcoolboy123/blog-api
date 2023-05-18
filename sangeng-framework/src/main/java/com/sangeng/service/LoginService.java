package com.sangeng.service;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;

/**
 * @author ljy
 * @date 2023/2/8
 */
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

}
