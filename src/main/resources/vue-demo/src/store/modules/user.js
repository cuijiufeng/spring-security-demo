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
            if(r.children[0]) {
              r.redirect = r.children[0].path;
            }
            return r.children.length != 0;
          }
          let permission = permissions.find(p => p.menuCode == r.meta.menu);
          //给路由写name
          if(undefined != permission) {
            r.meta.title = permission.menuName;
          }
          return undefined != permission;
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