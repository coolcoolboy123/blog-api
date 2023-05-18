package com.sangeng.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ljy
 * @date 2023/2/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCategoryVo {
    @ExcelProperty("分类名")
    private String name;
    @ExcelProperty("描述")
    private String description;
    /**
     * 状态0:正常,1禁用
     */
    @ExcelProperty("状态0:正常,1禁用")
    private String status;
}
