package io.inferiority.demo.springsecurity.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.InitializationMapper;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IPermissionService;
import io.inferiority.demo.springsecurity.service.IRoleService;
import io.inferiority.demo.springsecurity.utils.AuthContextUtil;
import io.inferiority.demo.springsecurity.utils.PermissionCompareUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    @Autowired
    private SnowflakeGenerator snowflakeGenerator;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InitializationMapper initializationMapper;
    @Autowired
    private IPermissionService permissionService;

    @Override
    public PageInfo<RoleEntity> list(PageDto page, RoleEntity searchRole) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<RoleEntity> roles = roleMapper.selectList(Wrappers.<RoleEntity>lambdaQuery()
                .likeRight(StringUtils.isNotBlank(searchRole.getRoleName()), RoleEntity::getRoleName, searchRole.getRoleName())
                .eq(StringUtils.isNotBlank(searchRole.getRoleKey()), RoleEntity::getRoleKey, defaultRolePrefix + searchRole.getRoleKey())
                .between(ArrayUtils.isNotEmpty(page.getDateRange()), RoleEntity::getCreateTime, page.getStart(), page.getEnd())
                .orderByDesc(RoleEntity::getCreateTime));
        PageInfo<RoleEntity> pageInfo = new PageInfo<>(roles);
        pageInfo.getList().forEach(role -> role.setRoleKey(role.getRoleKey().replace(defaultRolePrefix, "")));
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(RoleEntity role, List<String> pids) {
        Date currentTime = new Date();
        //判断权限是否足够
        PermissionCompareUtil.compare(initializationMapper.selectOne(Wrappers.lambdaQuery()).getSuperUserId(), null, role);
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
            permissionService.editPermission(role.getId(), pids);
            return;
        }
        if (roleMapper.selectCount(Wrappers.<RoleEntity>lambdaQuery()
                .eq(RoleEntity::getRoleKey, role.getRoleKey())) > 0) {
            throw new ServiceException(ErrorEnum.EXIST_ROLE_FAILED);
        }
        role.setId(snowflakeGenerator.next().toString());
        role.setCreateUser(AuthContextUtil.currentRole().getId());
        role.setCreateTime(currentTime);
        if (roleMapper.insert(role) != 1) {
            throw new ServiceException(ErrorEnum.ADD_EDIT_ROLE_FAILED);
        }
        permissionService.editPermission(role.getId(), pids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        PermissionCompareUtil.compareByRoles(initializationMapper.selectOne(Wrappers.lambdaQuery()).getSuperUserId(), roleMapper.selectBatchIds(ids));
        if (roleMapper.deleteBatchIds(ids) < 1) {
            throw new ServiceException(ErrorEnum.DELETE_ROLE_FAILED);
        }
    }
}
