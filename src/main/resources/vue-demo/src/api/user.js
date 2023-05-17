import http from "@/utils/axios";
import {sha256Hashed} from '@/utils/crypto';

//用户分页
export const apiUserList = (param) => {
  return http({
    method: 'GET',
    url: '/user/list',
    data: param
  })
}

//新增/编辑用户
export const apiEditUser = (param) => {
  let newObj = JSON.parse(JSON.stringify(param));
  newObj.password = sha256Hashed(newObj.password, newObj.username);
  return http({
    method: 'POST',
    url: '/user/edit',
    data: newObj
  });
}