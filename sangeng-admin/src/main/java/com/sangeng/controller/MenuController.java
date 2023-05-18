package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.MenuTreeVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.MenuService;
import com.sangeng.utils.SystemConverter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljy
 * @date 2023/2/11
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    /**
    * 菜单列表
    * @param status 状态
    * @param menuName  菜单名
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/list")
    public ResponseResult getMenuList(String status,String menuName){
        return menuService.getMenuList(status,menuName);
    }

    /**
    * 新增菜单
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        return ResponseResult.okResult(menuService.save(menu));
    }

    /**
    * 根据id查询菜单数据
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable("id") String id){
        return menuService.getMenuById(id);
    }

    /**
    * 更新菜单
    * @param menu
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        if (menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_MENU_ERROR);
        }
        return ResponseResult.okResult(menuService.updateById(menu));
    }

    /**
    * 删除菜单
    * @param menuId
    * @return com.sangeng.domain.ResponseResult
    */
    @DeleteMapping("/{menuId}")
    public ResponseResult removeMenu(@PathVariable("menuId") Long menuId){
        if (menuService.hasChild(menuId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.REMOVE_MENU_ERROR);
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }

    /**
     * 获取菜单树接口
     * @return com.sangeng.domain.ResponseResult
     */
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }


    /**
     * 加载对应角色菜单列表树接口
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselectById(@PathVariable("id") Long id){
        return menuService.roleMenuTreeselectById(id);
    }
}
