package com.sangeng.domain.vo;

import com.sangeng.domain.entity.Role;
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
public class UserRoleInfoVo {
    private List<Long> roleIds;

    private List<Role> roles;

    private UserInfoVo user;
}
