package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddRoleDto;
import com.sangeng.domain.dto.ChangeRoleStatusDto;
import com.sangeng.domain.dto.UpdateRoleDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author ljy
 * @date 2023/2/11
 */
@RestController
@RequestMapping("system/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
    * 分页查询角色列表
    * @param pageNum, pageSize, roleName, status
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/list")
    public ResponseResult listRolePage(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.listRolePage(pageNum,pageSize,roleName,status);
    }

    /**
    * 改变角色状态
    * @param changeRoleStatusDto
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        Role role = new Role();
        role.setId(changeRoleStatusDto.getRoleId());
        role.setStatus(changeRoleStatusDto.getStatus());
        roleService.updateById(role);
        return ResponseResult.okResult();
    }

    /**
    * 添加角色
    * @param addRoleDto
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    /**
    * 角色信息回显接口
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/{id}")
    public ResponseResult getRoleInfo(@PathVariable("id") Long id){
        if (Objects.nonNull(id)) {
            return roleService.getRoleInfo(id);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
    * 删除固定的某个角色（逻辑删除）
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @DeleteMapping("/{id}")
    public ResponseResult removeRoleById(@PathVariable("id") Long id){
        if (Objects.nonNull(id)) {
            return roleService.removeRoleById(id);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
    * 查询角色列表接口
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return ResponseResult.okResult(roleService.list(null));
    }


    /**
    *更新角色信息接口
    * @param updateRoleDto
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }
}
