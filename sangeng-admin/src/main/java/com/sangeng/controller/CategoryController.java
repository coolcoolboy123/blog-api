package com.sangeng.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.ExcelCategoryVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.CategoryService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ljy
 * @date 2023/2/10
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
    * 查询所有分类接口
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void  export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
    * 分页查询分类列表
    * @param pageNum, pageSize, name, status
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/list")
    public ResponseResult listPageCategory(Integer pageNum,Integer pageSize,String name,String status){
        return  categoryService.listPageCategory(pageNum,pageSize,name,status);
    }

    /**
    * 新增分类
    * @param category
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping
    public ResponseResult add(@RequestBody Category category){
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    /**
    * 根据id查询分类
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") Long id){
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }

    /**
    * 更新分类
    * @param category
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping
    public ResponseResult update(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("{id}")
    public ResponseResult removeById(@PathVariable("id") Long id){
        Category category = categoryService.getById(id);
        categoryService.removeById(category.getId());
        return ResponseResult.okResult();
    }
}
