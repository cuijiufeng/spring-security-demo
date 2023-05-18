package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IRoleService;
import io.inferiority.demo.springsecurity.utils.AuthContextUtil;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import io.inferiority.demo.springsecurity.utils.PermissionCompareUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/15 23:33
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Value("${default.role.prefix:ROLE_}")
    private String defaultRolePrefix;
    @Value("${super.admin.user.id:1}")
    private String superAdminUserId;
    @Value("#{T(io.inferiority.demo.springsecurity.utils.CryptoUtil).parsePrivateKey('${jwt.priv.key:classpath:jwt/rsa.der}')}")
    private PrivateKey jwtPrivKey;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<RoleEntity> list(PageDto page, RoleEntity searchRole) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<RoleEntity> roles = roleMapper.selectList(Wrappers.<RoleEntity>lambdaQuery()
                .likeRight(StringUtils.isNotBlank(searchRole.getRoleName()), RoleEntity::getRoleName, searchRole.getRoleName())
                .eq(StringUtils.isNotBlank(searchRole.getRoleKey()), RoleEntity::getRoleKey, defaultRolePrefix + searchRole.getRoleKey())
                .between(ArrayUtils.isNotEmpty(page.getDateRange()), RoleEntity::getCreateTime, page.getStart(), page.getEnd())
                .orderByAsc(RoleEntity::getCreateTime));
        PageInfo<RoleEntity> pageInfo = new PageInfo<>(roles);
        pageInfo.getList().forEach(role -> role.setRoleKey(role.getRoleKey().replace(defaultRolePrefix, "")));
        return pageInfo;
    }

    @Override
    @Transactional
    public void edit(RoleEntity role) {
        Date currentTime = new Date();
        //判断权限是否足够
        PermissionCompareUtil.compare(superAdminUserId, role);
        role.setRoleKey(defaultRolePrefix + role.getRoleKey());
        //编辑
        if (StringUtils.isNotBlank(role.getId())) {
            RoleEntity originalRole = roleMapper.selectOne(Wrappers.<RoleEntity>lambdaQuery()
                    .eq(RoleEntity::getId, role.getId()));
            //不允许的值
            if (!originalRole.getRoleKey().equals(role.getRoleKey())) {
                throw new ServiceException(ErrorEnum.CANT_MODIFY_ROLE_KEY_FAILED);
            }
            if (roleMapper.updateById(role) != 1) {
                throw new ServiceException(ErrorEnum.ADD_EDIT_ROLE_FAILED);
            }
            return;
        }
        if (roleMapper.selectCount(Wrappers.<RoleEntity>lambdaQuery()
                .eq(RoleEntity::getRoleKey, role.getRoleKey())) > 0) {
            throw new ServiceException(ErrorEnum.EXIST_ROLE_FAILED);
        }
        role.setId(SnowflakeId.generateStrId());
        role.setCreateUser(AuthContextUtil.currentRole().getId());
        role.setCreateTime(currentTime);
        if (roleMapper.insert(role) != 1) {
            throw new ServiceException(ErrorEnum.ADD_EDIT_ROLE_FAILED);
        }
    }

    @Override
    @Transactional
    public void delete(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        //先取消用户才能删除角色
        if (userMapper.selectCount(Wrappers.<UserEntity>lambdaQuery()
                .in(UserEntity::getRoleId, ids)) > 0) {
            throw new ServiceException(ErrorEnum.DELETE_THIS_ROLE_USER_FAILED);
        }
        //判断权限是否足够
        PermissionCompareUtil.compare(superAdminUserId, roleMapper.selectBatchIds(ids));
        if (roleMapper.deleteBatchIds(ids) < 1) {
            throw new ServiceException(ErrorEnum.DELETE_ROLE_FAILED);
        }
        //如果删除了当前角色则退出重新登录
        if (ids.contains(AuthContextUtil.currentRole().getId())) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            response.setHeader(JwtUtil.TOKEN_HEADER, JwtUtil.createJwt(jwtPrivKey, null, 0));
        }
    }
}
