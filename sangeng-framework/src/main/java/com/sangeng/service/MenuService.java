package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-02-09 13:50:14
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
    /**
     * 菜单列表
     * @param status 状态
     * @param menuName  菜单名
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult getMenuList(String status, String menuName);
    /**
     * 根据id查询菜单数据
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    ResponseResult getMenuById(String id);

    boolean hasChild(Long menuId);
    /**
     * 获取菜单树接口
     * @return com.sangeng.domain.ResponseResult
     */
    List<Menu> selectMenuList(Menu menu);

    ResponseResult roleMenuTreeselectById(Long id);
}
