import http from "@/utils/axios";

//用户分页
export const apiRoleList = (param) => {
  return http({
    method: 'GET',
    url: '/role/list',
    data: param
  })
}