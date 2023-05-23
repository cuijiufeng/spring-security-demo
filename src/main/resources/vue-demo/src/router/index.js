import {createRouter, createWebHashHistory} from 'vue-router'
import each from "./each";
import routes from "./routes";

// 公共路由
export const defaultRoutes = [
  {
    path: "/404",
    name: "404",
    component: () => import("@/views/404")
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/Login"),
  },
]

// 动态路由
export const dynamicRoute = {
  path: "/",
  name: "Root",
  component: () => import("@/layout/index"),
  children: routes
}

const router = createRouter({
  history: createWebHashHistory(),
  routes: defaultRoutes
})

//加载路由守卫前置守卫
router.beforeEach(each.beforeEach);
//加载路由后置守卫
router.afterEach(each.afterEach);

export default router
