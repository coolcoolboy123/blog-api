package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.dto.UpdateUserDto;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author ljy
 * @date 2023/2/12
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
    * 分页查询用户列表
    * @param pageNum, pageSize, userName, phonenumber, status
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/list")
    public ResponseResult listUserPage(Integer pageNum, Integer pageSize, String userName,String phonenumber, String status){
        return userService.listUserPage(pageNum,pageSize,userName,phonenumber,status);
    }

    /**
    *  新增用户
    * @param addUserDto
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        //非空判断
        if (!StringUtils.hasText(addUserDto.getUserName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!StringUtils.hasText(addUserDto.getEmail())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getPhonenumber())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONENUMBER_NOT_NULL);
        }
        return userService.addUser(addUserDto);
    }

    /**
    * 删除固定的某个用户（逻辑删除）
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @DeleteMapping("{id}")
    public ResponseResult removeUserById(@PathVariable("id") Long id){
        return userService.removeUserById(id);
    }

    /**
     * 根据id查询用户信息回显接口
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @GetMapping("/{id}")
    public ResponseResult getUserInfo(@PathVariable("id") Long id){
        if (Objects.nonNull(id)) {
            return userService.getUserInfo(id);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
    * 更新用户信息接口
    * @param updateUserDto
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
