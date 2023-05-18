import store from "@/store";
import axios from "axios";
import qs from 'qs'
import {AUTHENTICATION, LANGUAGE} from './config';

/*
使用示例
http({method:'GET',url: '/test'}).then(([data]) => {
  console.log(data)
}).catch(([err]) => {
  console.log(err)
})
*/

/*
param: {
  method  //请求方法
  url     //请求路径 
  headers //请求头
  responseType //返回类型
  root    //是否返回全部的响应数据
  data    //请求数据
}
*/
export default (param) => {
  //header
  let headers = {};
  headers[LANGUAGE] = localStorage.getItem(LANGUAGE);
  headers[AUTHENTICATION] = localStorage.getItem(AUTHENTICATION);
  for(let key in param.headers) {
    headers[key] = param.headers[key];
  }
  return new Promise((resolve, reject) => {
    //请求参数
    let getParam = '';
    let postParam = new FormData();
    if(param.method.toUpperCase() == 'GET') {
      getParam = '?t='+new Date().getTime() + (param.data ? '&'+qs.stringify(param.data) : '');
    } else if (param.method.toUpperCase() == 'POST') {
      for(let key in param.data){
        if(param.data[key] !== undefined) {
          postParam.append(key, param.data[key]);
        }
      }
    }
    //请求
    axios({
      timeout: 60000,
      baseURL: import.meta.env.VITE_APP_BASE_API,
      url: `${param.url}${getParam}`,
      method: param.method,
      headers: headers,
      responseType: param.responseType ? param.responseType : '',
      data: postParam,
      withCredentials: true
    }).then(resp => {
      let result = resp.data;
      let headers = resp.headers;
      if(headers[AUTHENTICATION]) {
        localStorage.setItem(AUTHENTICATION, headers[AUTHENTICATION]);
      }
      if(param.root) {
        resolve([result, headers]);
      }
      if (result.code == 200) {
        resolve([result.data, headers]);
      } else {
        reject([result.data, headers]);
        if(result.code == 401) {
          store.commit('logout')
        }
      }
    }).catch(err => {
      // router.replace("/500");
      // reject(err.response);
      console.log(err.response);
    })
  });
}