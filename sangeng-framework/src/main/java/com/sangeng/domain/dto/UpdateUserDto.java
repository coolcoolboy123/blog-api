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
public class UpdateUserDto {
    private String email;

    private Long id;

    private String nickName;

    private String sex;

    private String status;

    private String userName;

    private List<Long> roleIds;
}
