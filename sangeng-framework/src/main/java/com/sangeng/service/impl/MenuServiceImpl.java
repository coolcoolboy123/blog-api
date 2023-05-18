package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.MenuListVo;
import com.sangeng.domain.vo.MenuTreeVo;
import com.sangeng.domain.vo.RoleMenuTreeSelectVo;
import com.sangeng.domain.vo.SelectMenuVo;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import com.sangeng.utils.SystemConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-02-09 13:50:14
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            return menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            //如果是 返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else {
            //否则 获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先构建第一层的菜单 然后去找他们的子菜单设置到children属性中
        return builderMenuTree(menus,0L);
    }

    /**
     * 菜单列表
     * @param status 状态
     * @param menuName  菜单名
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult getMenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        wrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        wrapper.orderByAsc(Menu::getOrderNum,Menu::getParentId);
        List<Menu> menuList = list(wrapper);
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(menuList, MenuListVo.class);
        return ResponseResult.okResult(menuListVos);
    }

    /**
     * 根据id查询菜单数据
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult getMenuById(String id) {
        Menu menu = getById(id);
        SelectMenuVo selectMenuVo = BeanCopyUtils.copyBean(menu, SelectMenuVo.class);
        return ResponseResult.okResult(selectMenuVo);
    }

    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,menuId);
        return count(wrapper) != 0;
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        return menus;
    }

    /**
     * 加载对应角色菜单列表树接口
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult roleMenuTreeselectById(Long id) {
        List<Menu> menus = selectMenuList(new Menu());
        List<Long> checkedKeys = selectMenuListByRoleId(id);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }

    private List<Long> selectMenuListByRoleId(Long id) {
        return baseMapper.selectMenuListByRoleId(id);
    }

    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
    * 获取传入参数的 子Menu集合
    * @param menu
    * @param menus
    * @return java.util.List<com.sangeng.domain.entity.Menu>
    */
    private List<Menu> getChildren(Menu menu,List<Menu> menus) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
    }
}
