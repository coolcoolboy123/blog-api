package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-12 16:07:46
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
