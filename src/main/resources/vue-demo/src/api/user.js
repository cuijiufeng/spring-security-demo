import http from "@/utils/axios";

//用户分页
export const userList = (param) => {
  return http({
    method: 'GET',
    url: '/user/list',
    data: param
  })
}