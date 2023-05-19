package io.inferiority.demo.springsecurity.service;

import io.inferiority.demo.springsecurity.model.vo.PermissionVo;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:58
 */
public interface IPermissionService {
    List<PermissionVo> permissionsTree(String parentId);

    void editPermission(String roleId, List<String> pids);

    List<String> have(String roleId);
}
