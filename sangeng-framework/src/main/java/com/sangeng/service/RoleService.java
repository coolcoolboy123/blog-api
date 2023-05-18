package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddRoleDto;
import com.sangeng.domain.dto.UpdateRoleDto;
import com.sangeng.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-02-09 13:57:10
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRolePage(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleInfo(Long id);

    ResponseResult removeRoleById(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);
}
