import system from './system';

export default [
  {
    path: "/index",
    name: "Index",
    meta: {
      menu: 'index',
      icon: 'home',
    },
    component: () => import("@/views/Example"),
  }, 
  system,
  {
    path: "/log",
    name: "Log",
    meta: {
      menu: 'log',
      icon: 'form',
    },
    component: () => import("@/views/Log"),
  }
]