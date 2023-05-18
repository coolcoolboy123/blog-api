package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.TagListDto;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.TagVo;
import com.sangeng.service.TagService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ljy
 * @date 2023/2/8
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 查询标签列表
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param tagListDto tagDto封装类
     * @return com.sangeng.domain.ResponseResult<com.sangeng.domain.vo.PageVo>
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    /**
     * 新增标签
     *
     * @param tag
     * @return com.sangeng.domain.ResponseResult
     */
    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag) {
        return tagService.addTag(tag);
    }

    /**
     * 删除标签
     *
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @DeleteMapping("/{id}")
    public ResponseResult removeTag(@PathVariable("id") Long id) {
        return tagService.removeTag(id);
    }

    /**
     * 获取标签信息
     *
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @GetMapping("/{id}")
    public ResponseResult getTagInfo(@PathVariable("id") Long id) {
        return tagService.getTagInfo(id);
    }

    /**
     * 修改标签接口
     *
     * @param tag
     * @return com.sangeng.domain.ResponseResult
     */
    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }

    /**
    * 查询所有标签接口
    * @return com.sangeng.domain.ResponseResult
    */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
