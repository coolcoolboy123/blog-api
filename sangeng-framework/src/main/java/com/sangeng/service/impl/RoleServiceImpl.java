package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddRoleDto;
import com.sangeng.domain.dto.UpdateRoleDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.RolePageVo;
import com.sangeng.domain.vo.RoleVo;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.service.RoleMenuService;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-02-09 13:57:10
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户当前所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listRolePage(Integer pageNum, Integer pageSize, String roleName, String status) {
        //查询
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        wrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        wrapper.orderByAsc(Role::getRoleSort);
        List<Role> roleList = list(wrapper);
        //分页
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        //封装成vo
        List<RolePageVo> rolePageVos = BeanCopyUtils.copyBeanList(roleList, RolePageVo.class);
        PageVo pageVo = new PageVo(rolePageVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 添加角色
     * @param addRoleDto
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        if (Objects.nonNull(addRoleDto.getMenuIds())) {
            List<RoleMenu> collect = addRoleDto.getMenuIds().stream()
                    .map(menuId -> new RoleMenu(role.getId(), menuId))
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(collect);
        }
        return ResponseResult.okResult();
    }

    /**
     * 角色信息回显接口
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult getRoleInfo(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    /**
     * 删除固定的某个角色（逻辑删除）
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult removeRoleById(Long id) {
        Role role = getById(id);
        removeById(role.getId());
        return ResponseResult.okResult();
    }

    /**
     *更新角色信息接口
     * @param updateRoleDto
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        //更新
        Role role = getById(updateRoleDto.getId());
        updateById(role);
        //删除原有的
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuService.remove(wrapper);
        //添加新的关联
        List<RoleMenu> roleMenuList = updateRoleDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }
}
