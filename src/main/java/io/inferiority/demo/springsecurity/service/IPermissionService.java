package io.inferiority.demo.springsecurity.service;

import io.inferiority.demo.springsecurity.model.Permission;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:58
 */
public interface IPermissionService {
    List<Permission> allPermissions();
}