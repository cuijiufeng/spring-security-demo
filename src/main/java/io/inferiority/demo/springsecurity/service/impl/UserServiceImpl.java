package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import io.inferiority.demo.springsecurity.service.IUserService;
import io.inferiority.demo.springsecurity.utils.AuthContextUtil;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @date 2023/5/15 19:19
 */
@Service
public class UserServiceImpl implements IUserService {
    @Value("${super.admin.user.id:1}")
    private String superAdminUserId;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<UserVo> list(PageDto page, UserEntity searchUser) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<UserVo> users = userMapper.selectListUserVo(Wrappers.<UserEntity>lambdaQuery()
                .select(UserEntity.class, c -> true)
                .eq(StringUtils.isNotBlank(searchUser.getRoleId()), UserEntity::getRoleId, searchUser.getRoleId())
                .eq(Objects.nonNull(searchUser.getSex()), UserEntity::getSex, searchUser.getSex())
                .likeRight(StringUtils.isNotBlank(searchUser.getUsername()), UserEntity::getUsername, searchUser.getUsername())
                .likeRight(StringUtils.isNotBlank(searchUser.getPhoneNumber()), UserEntity::getPhoneNumber, searchUser.getPhoneNumber())
                .between(ArrayUtils.isNotEmpty(page.getDateRange()), UserEntity::getCreateTime, page.getStart(), page.getEnd())
                .orderByAsc(UserEntity::getCreateTime));
        return new PageInfo<>(users);
    }

    @Override
    @Transactional
    public void edit(UserEntity user, String originalPassword) {
        Date currentTime = new Date();
        //判断权限是否足够
        if (StringUtils.isNotBlank(user.getRoleId())
                && !AuthContextUtil.currentRole().getId().equals(user.getRoleId())
                && AuthContextUtil.currentRole().getLevel() > roleMapper.selectById(user.getRoleId()).getLevel()) {
            throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
        }
        //编辑
        if (StringUtils.isNotBlank(user.getId())) {
            UserEntity originalUser = userMapper.selectOne(Wrappers.<UserEntity>lambdaQuery()
                    .eq(UserEntity::getUsername, user.getUsername()));
            //不允许修改用户名
            if (!originalUser.getUsername().equals(user.getUsername())) {
                throw new ServiceException(ErrorEnum.CANT_MODIFY_USERNAME_FAILED);
            }
            //不允许修改超级管理员的某些属性
            if (originalUser.getId().equals(superAdminUserId) &&
                    (!originalUser.getRoleId().equals(user.getRoleId())
                            || !originalUser.getEnabled().equals(user.getEnabled()))) {
                throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
            }

            //修改密码
            if (StringUtils.isNotBlank(user.getPassword())) {
                if (StringUtils.isBlank(originalPassword)
                        || !passwordEncoder.matches(originalPassword, originalUser.getPassword())) {
                    throw new ServiceException(ErrorEnum.ORIGINAL_PASSWORD_NOT_MATCH_FAILED);
                }
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user.setLastUpdatePasswordTime(currentTime);
            if (userMapper.updateById(user) != 1) {
                throw new ServiceException(ErrorEnum.ADD_EDIT_USER_FAILED);
            }
            return;
        }
        //新增
        List<UserEntity> existUser = userMapper.selectList(Wrappers.<UserEntity>lambdaQuery()
                .eq(UserEntity::getUsername, user.getUsername()));
        if (!CollectionUtils.isEmpty(existUser)) {
            throw new ServiceException(ErrorEnum.EXIST_USER_FAILED);
        }
        user.setId(SnowflakeId.generateStrId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(currentTime);
        user.setLastUpdatePasswordTime(currentTime);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        if (userMapper.insert(user) != 1) {
            throw new ServiceException(ErrorEnum.ADD_EDIT_USER_FAILED);
        }
    }

    @Override
    public void delete(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        if (userIds.contains(superAdminUserId)) {
            throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
        }
        if (userMapper.deleteBatchIds(userIds) < 1) {
            throw new ServiceException(ErrorEnum.DELETE_USER_FAILED);
        }
    }
}
