import router, {dynamicRoute} from '@/router'
import {AUTHENTICATION, LOGIN_INFO} from '@/utils/config'

export default {
  state: {
    currentUser: {
      username: '',
    },
  },
  getters: {
    currentUser: (state) => { return state.currentUser; },
    currentPermission: (state) => { return state.currentUser.permissions; }
  },
  mutations: {
    login(state, loginData) {
      state.currentUser = loginData;
      let currentPermission = loginData.permissions;
      sessionStorage.setItem(LOGIN_INFO, JSON.stringify(loginData));
      //生成用户权限路由
      recursionFilterRoute(dynamicRoute, currentPermission);
      //添加路由router
      router.addRoute(dynamicRoute);
      router.replace('/');

      //递归过滤路由
      function recursionFilterRoute(route, permissions) {
        if(!route.children) {
          return;
        }
        route.children = route.children.filter(r => {
          if(r.children) {
            recursionFilterRoute(r, permissions);
            //默认路由
            r.redirect = r.children[0].path;
          }
          return undefined != permissions.find(p => p.menuCode == r.meta.menu);
        });
      }
    },
    logout(state) {
      router.replace('/login');
      sessionStorage.removeItem(LOGIN_INFO);
      localStorage.removeItem(AUTHENTICATION);
      state.currentUser = {
        username: '',
      };
    }
  },
  actions: {
  },
}