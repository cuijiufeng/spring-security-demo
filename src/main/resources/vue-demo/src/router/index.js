import {createRouter, createWebHashHistory} from 'vue-router'

// 公共路由
export const constantRoutes = [
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
  {
    path: "/",
    name: "Root",
    component: () => import("@/layout/index"),
    // children: routers
  }
]

// 动态路由
export const dynamicRoutes = [
  {
    path: "/",
    name: "Root",
    component: () => import("@/layout/index"),
    // children: routers
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes
})

//加载路由守卫前置守卫
// router.beforeEach(each.beforeEach);
//加载路由后置守卫
// router.afterEach(each.afterEach);

export default router
