import store from '@/store';
import router from './index';
import {ROUTER_WHITE_LIST} from '@/utils/config'

export default {
  //路由前置守卫
  beforeEach(to, from, next) {
    //路由拦截白名单
    if(ROUTER_WHITE_LIST.find(e => {return e == to.name})) {
      next();
      return;
    }
    //当没有登录时显示登录页面
    if(store.getters.userInfo == undefined) {
      router.replace('/login');
      return;
    }
    //没有初始化系统时不准进入主页面
    if(to.fullPath == "/" && !store.getters.initialized) {
      router.replace('/init');
      return;
    }
    //当前页面不存在
    if (to.matched.length == 0) {
      router.push('/404');
      return;
    }
    next();
  },
  
  //路由后置守卫
  afterEach(to, from) {
  }
};
