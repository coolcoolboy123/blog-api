package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;
import com.sangeng.service.LinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/2/12
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    private LinkService linkService;

    /**
    * 分页查询友链列表
    * @param pageNum, pageSize, name, status
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/list")
    public ResponseResult listPage(Integer pageNum, Integer pageSize, String name, String status){
        return linkService.listPage(pageNum,pageSize,name,status);
    }

    /**
    * 新增友链
    * @param link
    * @return com.sangeng.domain.ResponseResult
    */
    @PostMapping
    public ResponseResult add(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    /**
    * 根据id查询友联
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    /**
    * 修改友链
    * @param link
    * @return com.sangeng.domain.ResponseResult
    */
    @PutMapping
    public ResponseResult update(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    /**
    * 删除友链
    * @param id
    * @return com.sangeng.domain.ResponseResult
    */
    @DeleteMapping("{id}")
    public ResponseResult remove(@PathVariable("id") Long id){
        Link link = linkService.getById(id);
        linkService.removeById(link.getId());
        return ResponseResult.okResult();
    }
}
