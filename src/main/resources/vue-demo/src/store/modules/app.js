import {LOGIN_INFO} from '@/utils/config'

export default {
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
    appCreate(context) {
      //如果已经登录，是刷新操作的时候恢复用户信息
      if(sessionStorage.getItem(LOGIN_INFO)) {
        context.commit('login', JSON.parse(sessionStorage.getItem(LOGIN_INFO)));
      }
    }
  },
}