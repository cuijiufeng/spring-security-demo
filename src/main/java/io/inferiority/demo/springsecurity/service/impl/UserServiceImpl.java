package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.InitializationMapper;
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
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import io.inferiority.demo.springsecurity.utils.PermissionCompareUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @date 2023/5/15 19:19
 */
@Service
public class UserServiceImpl implements IUserService {
    @Value("#{T(io.inferiority.demo.springsecurity.utils.CryptoUtil).parsePrivateKey('${jwt.priv.key:classpath:jwt/rsa.der}')}")
    private PrivateKey jwtPrivKey;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private InitializationMapper initializationMapper;
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
                .orderByDesc(UserEntity::getCreateTime));
        return new PageInfo<>(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(UserEntity user, String originalPassword) {
        Date currentTime = new Date();
        //判断权限是否足够
        if (StringUtils.isNotBlank(user.getRoleId())) {
            PermissionCompareUtil.compare(initializationMapper.selectOne(Wrappers.lambdaQuery()).getSuperUserId(),
                    user.getId(),
                    roleMapper.selectById(user.getRoleId()));
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
            if (originalUser.getId().equals(initializationMapper.selectOne(Wrappers.lambdaQuery()).getSuperUserId())
                    && (!originalUser.getRoleId().equals(user.getRoleId()) || !originalUser.getEnabled().equals(user.getEnabled()))) {
                throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
            }

            //修改密码
            if (StringUtils.isNotBlank(user.getPassword())) {
                if (StringUtils.isBlank(originalPassword) || !passwordEncoder.matches(originalPassword, originalUser.getPassword())) {
                    throw new ServiceException(ErrorEnum.ORIGINAL_PASSWORD_NOT_MATCH_FAILED);
                }
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user.setLastUpdatePasswordTime(currentTime);
            if (userMapper.updateById(user) != 1) {
                throw new ServiceException(ErrorEnum.ADD_EDIT_USER_FAILED);
            }
            //修改了自己的状态退出登录
            if(!user.getEnabled() && user.getUsername().equals(AuthContextUtil.currentUsername())) {
                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
                response.setHeader(JwtUtil.TOKEN_HEADER, JwtUtil.createJwt(jwtPrivKey, null, 0));
            }
            return;
        }
        //新增
        if (userMapper.selectCount(Wrappers.<UserEntity>lambdaQuery()
                .eq(UserEntity::getUsername, user.getUsername())) > 0) {
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
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        if (userIds.contains(initializationMapper.selectOne(Wrappers.lambdaQuery()).getSuperUserId())) {
            throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
        }
        //判断权限是否足够
        PermissionCompareUtil.compareByUsers(initializationMapper.selectOne(Wrappers.lambdaQuery()).getSuperUserId(),
                userMapper.selectListUserVo(Wrappers.<UserEntity>lambdaQuery()
                        .select(UserEntity.class, i -> true)
                        .in(UserEntity::getId, userIds)));
        if (userMapper.deleteBatchIds(userIds) < 1) {
            throw new ServiceException(ErrorEnum.DELETE_USER_FAILED);
        }
        //如果删除了当前角色则退出重新登录
        if (userIds.contains(AuthContextUtil.currentUser().getId())) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            response.setHeader(JwtUtil.TOKEN_HEADER, JwtUtil.createJwt(jwtPrivKey, null, 0));
        }
    }
}
