import http from "@/utils/axios";

//用户分页
export const apiRoleList = (param) => {
  return http({
    method: 'GET',
    url: '/role/list',
    data: param
  })
}

//新增/编辑角色
export const apiEditRole = (param) => {
  return http({
    method: 'POST',
    url: '/role/edit',
    data: param
  });
}

//删除角色
export const apiDeleteRole = (param) => {
  return http({
    method: 'POST',
    url: '/role/delete',
    data: param
  });
}

//权限树
export const apiPermissionsTree = (param) => {
  return http({
    method: 'GET',
    url: '/permission/tree',
    data: param
  });
}

//角色拥有的权限
export const apiHavePermissions = (param) => {
  return http({
    method: 'GET',
    url: '/permission/have',
    data: param
  });
}