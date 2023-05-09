import http from "@/utils/axios";
import {sha256Hashed} from '@/utils/crypto'

//登录接口
export const login = (param) => {
  let newObj = JSON.parse(JSON.stringify(param));
  newObj.password = sha256Hashed(newObj.password, newObj.username);
  return http({
    method: 'POST',
    url: '/auth/login',
    data: newObj
  })
}

//登录接口
export const logout = (param) => {
  return http({
    method: 'POST',
    url: '/auth/logout',
    data: param
  })
}