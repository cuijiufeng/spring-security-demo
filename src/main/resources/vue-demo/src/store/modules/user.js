import {defineStore} from 'pinia';
import router, {dynamicRoute} from '@/router'
import {AUTHENTICATION, LOGIN_INFO, ROUTE_TAGS} from '@/utils/config'

const useUserStore = defineStore(
  'user',
  {
    state: () => ({
      currentUser: {
        username: '',
      }
    }),
    actions: {
      login(loginData) {
        this.currentUser = loginData;
        let currentPermission = loginData.permissions;
        sessionStorage.setItem(LOGIN_INFO, JSON.stringify(loginData));
        //生成用户权限路由
        recursionFilterRoute(dynamicRoute, currentPermission);
        if(dynamicRoute.children[0]) {
          let defaultRoute = dynamicRoute.children[0];
          sessionStorage.setItem(ROUTE_TAGS, sessionStorage.getItem(ROUTE_TAGS) 
            || JSON.stringify([{'title': defaultRoute.meta.title, 'path': defaultRoute.path, 'effect': 'dark'}]))
        }
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
            }
            let permission = permissions.find(p => p.code == r.meta.menu);
            //路由title
            if(undefined != permission) {
              r.meta.title = permission.name;
            }
            return (undefined != permission) && (!permission.menuHidden);
          });
        }
      },
      logout() {
        router.replace('/login');
        sessionStorage.removeItem(LOGIN_INFO);
        localStorage.removeItem(AUTHENTICATION);
        sessionStorage.removeItem(ROUTE_TAGS);
        this.currentUser = {
          username: '',
        };
        //刷新页面，重置dynamicRoute
        location.reload();
      }
    }
  }
)

export default useUserStore