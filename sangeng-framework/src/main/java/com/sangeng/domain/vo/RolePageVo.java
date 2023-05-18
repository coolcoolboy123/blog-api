package com.sangeng.domain.vo;

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
public class RolePageVo {
    private Long id;

    private String roleKey;

    private String roleName;

    private Integer roleSort;

    private String status;
}
