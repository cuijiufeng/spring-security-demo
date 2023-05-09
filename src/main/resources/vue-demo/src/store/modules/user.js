import router, {dynamicRoute} from '@/router'
import {LOGIN_INFO} from '@/utils/config'

export default {
  state: {
    currentUser: undefined,
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
      state.currentUser = undefined;
      sessionStorage.removeItem(LOGIN_INFO);
      router.replace('/login');
      //此行是为了重路由，但是退出登录之后也进不去主页了，重新登录之后又会重新生成路由，所以删除此行
      location.reload();
    }
  },
  actions: {
  },
}