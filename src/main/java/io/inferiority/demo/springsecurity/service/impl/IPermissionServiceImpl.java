package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.dao.RolePermissionMapper;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.model.RolePermissionEntity;
import io.inferiority.demo.springsecurity.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:58
 */
@Service
public class IPermissionServiceImpl implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<PermissionEntity> allPermissions() {
        return permissionMapper.selectList(Wrappers.lambdaQuery());
    }

    @Override
    public List<PermissionEntity> roleHasPermissions(String roleId) {
        List<RolePermissionEntity> rolePermissions = rolePermissionMapper.selectList(Wrappers.<RolePermissionEntity>lambdaQuery()
                .eq(RolePermissionEntity::getRId, roleId));
        return rolePermissions.isEmpty()
                ? Collections.emptyList()
                : permissionMapper.selectBatchIds(rolePermissions.stream().map(RolePermissionEntity::getPId).collect(Collectors.toList()));
    }
}
