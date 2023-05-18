package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.dto.UpdateUserDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.entity.UserRole;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.UserInfoVo;
import com.sangeng.domain.vo.UserPageVo;
import com.sangeng.domain.vo.UserRoleInfoVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.UserMapper;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserRoleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-02-05 14:20:53
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;


    /**
     * 个人信息查询
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    /**
     * 更新用户信息
     * @param user 用户实体
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 用户注册
     * @param user 用户实体
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 分页查询用户列表
     * @param pageNum, pageSize, roleName, status
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult listUserPage(Integer pageNum, Integer pageSize, String userName, String phonenumber,String status) {
        //查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        wrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        wrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        List<User> list = list(wrapper);
        //分页
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        //封装返回
        List<UserPageVo> userPageVos = BeanCopyUtils.copyBeanList(list, UserPageVo.class);
        PageVo pageVo = new PageVo(userPageVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     *  新增用户
     * @param addUserDto
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        //判断用户名是否存在
        if (userNameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //判断邮箱是否存在
        if (emailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //判断手机号是否存在
        if (phoneNumberExist(addUserDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setCreateTime(new Date());
        //存入数据库
        save(user);
        //关联表
        List<UserRole> roleList = addUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(roleList);
        return ResponseResult.okResult();
    }

    /**
     * 删除固定的某个用户（逻辑删除）
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult removeUserById(Long id) {
        Long userId = SecurityUtils.getUserId();
        if (id.equals(userId)){
            throw new SystemException(AppHttpCodeEnum.REMOVE_USER_ERROR);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询用户信息回显接口
     * @param id
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    public ResponseResult getUserInfo(Long id) {
        User user = getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //查出用户角色列表
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId,user.getId());
        List<UserRole> userRoleList = userRoleService.list(wrapper);
        //查出用户所关联的角色id列表
        List<Long> roleIds = userRoleList.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        //查出所有角色的列表
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        List<Role> roles = roleService.list(queryWrapper);
        //封装返回
        UserRoleInfoVo userRoleInfoVo = new UserRoleInfoVo(roleIds,roles,userInfoVo);

        return ResponseResult.okResult(userRoleInfoVo);
    }

    /**
     * 更新用户信息接口
     * @param updateUserDto
     * @return com.sangeng.domain.ResponseResult
     */
    @Override
    @Transactional
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        User user = getById(updateUserDto.getId());
        //删除之前的关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(wrapper);
        //关联
        List<UserRole> userRoleList = updateUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(),roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        //更新
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail,email);
        return count(wrapper)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName,nickName);
        return count(wrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        return count(wrapper)>0;
    }
    private boolean phoneNumberExist(String phonenumber) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhonenumber,phonenumber);
        return count(wrapper)>0;
    }
}
