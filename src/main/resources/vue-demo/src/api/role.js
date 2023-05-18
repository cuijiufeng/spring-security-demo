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