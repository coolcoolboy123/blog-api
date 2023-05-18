package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ljy
 * @date 2023/2/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListDto {
    private String name;

    private String remark;
}
