package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:58
 */
@Service
public class IPermissionServiceImpl implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionEntity> allPermissions() {
        return permissionMapper.selectList(Wrappers.lambdaQuery());
    }
}
