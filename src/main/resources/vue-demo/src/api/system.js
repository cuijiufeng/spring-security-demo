import http from "@/utils/axios";
import {sha256Hashed} from '@/utils/crypto'

//登录接口
export const apiLogin = (param) => {
  let newObj = JSON.parse(JSON.stringify(param));
  if(newObj.password) {
    newObj.password = sha256Hashed(newObj.password, newObj.username);
  }
  return http({
    method: 'POST',
    url: '/auth/login',
    data: newObj
  })
}

//登出接口
export const apiLogout = (param) => {
  return http({
    method: 'POST',
    url: '/auth/logout',
    data: param
  })
}