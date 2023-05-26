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
  if(newObj.password) {
    newObj.password = sha256Hashed(newObj.password, newObj.username);
  }
  if(newObj.originalPassword) {
    newObj.originalPassword = sha256Hashed(newObj.originalPassword, newObj.username);
  }
  return http({
    method: 'POST',
    url: '/user/edit',
    data: newObj
  });
}

//删除用户
export const apiDeleteUser = (param) => {
  return http({
    method: 'POST',
    url: '/user/delete',
    data: param
  });
}

//日志文件导出
export const apiUserExport = (param) => {
  return http({
    method: 'POST',
    url: '/user/export',
    data: param,
    binary: true
  });
}