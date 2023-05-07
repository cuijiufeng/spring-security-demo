import http from "@/utils/axios";
// import { sha256 } from '@/utils/crypt'

//登录接口
export const login = (param) => {
  //param.password = sha256(param.password, param.username);
  return http({
    method: 'POST',
    url: '/auth/login',
    data: param
  })
}