package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.dao.RolePermissionMapper;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.model.RolePermissionEntity;
import io.inferiority.demo.springsecurity.model.vo.PermissionVo;
import io.inferiority.demo.springsecurity.service.IPermissionService;
import io.inferiority.demo.springsecurity.utils.AuthContextUtil;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:58
 */
@Service
public class IPermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<PermissionVo> permissionsTree(String parentId) {
        return permissionMapper.selectTree(Wrappers.<PermissionEntity>lambdaQuery()
                .select(PermissionEntity::getId, PermissionEntity::getParentId, PermissionEntity::getName)
                .eq(PermissionEntity::getParentId, parentId))
                .stream()
                .peek(p -> p.setChildren(permissionsTree(p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> have(String roleId) {
        return rolePermissionMapper.selectList(Wrappers.<RolePermissionEntity>lambdaQuery()
                .eq(RolePermissionEntity::getRId, roleId))
                .stream()
                .map(RolePermissionEntity::getPId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPermission(String roleId, List<String> pids) {
        //不可以有多自己多的权限
        List<String> slefPids = rolePermissionMapper.selectList(Wrappers.<RolePermissionEntity>lambdaQuery()
                .eq(RolePermissionEntity::getRId, AuthContextUtil.currentRole().getId()))
                .stream()
                .map(RolePermissionEntity::getPId)
                .collect(Collectors.toList());
        if (!slefPids.containsAll(pids)) {
            throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
        }

        //查原有权限
        List<String> originalPids = rolePermissionMapper.selectList(Wrappers.<RolePermissionEntity>lambdaQuery()
                .eq(RolePermissionEntity::getRId, roleId))
                .stream()
                .map(RolePermissionEntity::getPId)
                .collect(Collectors.toList());

        //先删除多的权限
        List<String> redundant = originalPids.stream()
                .filter(p -> !pids.contains(p))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(redundant)) {
            rolePermissionMapper.delete(Wrappers.<RolePermissionEntity>lambdaQuery()
                    .eq(RolePermissionEntity::getRId, roleId)
                    .in(RolePermissionEntity::getPId, redundant));
        }

        //添加少的权限
        List<RolePermissionEntity> missing = pids.stream()
                .filter(pId -> !originalPids.contains(pId))
                .map(pId -> new RolePermissionEntity(SnowflakeId.generateStrId(), roleId, pId))
                .collect(Collectors.toList());
        saveBatch(missing);
    }
}
