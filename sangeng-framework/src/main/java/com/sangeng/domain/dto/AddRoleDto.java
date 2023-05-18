package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ljy
 * @date 2023/2/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleDto {
    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;

    private List<Long> menuIds;
    //备注
    private String remark;
}
