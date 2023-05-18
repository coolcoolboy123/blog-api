package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.dto.UpdateUserDto;
import com.sangeng.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-02-05 14:20:52
 */
public interface UserService extends IService<User> {
    /**
     * 个人信息查询
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult userInfo();
    /**
    * 更新用户信息
    * @param user 用户实体
    * @return com.sangeng.domain.ResponseResult
    */
    ResponseResult updateUserInfo(User user);
    /**
     * 用户注册
     * @param  user 用户实体
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult register(User user);

    ResponseResult listUserPage(Integer pageNum, Integer pageSize, String userName,String phonenumber,String status);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult removeUserById(Long id);

    ResponseResult getUserInfo(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}
