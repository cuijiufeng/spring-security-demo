
export default [
  {
    path: "/index",
    name: "Index",
    meta: {
      submenu: false,
      menu: 'index',
    },
    component: () => import("@/views/404"),
  }, 
  {
    path: "/help",
    name: "Help",
    meta: {
      submenu: false,
      menu: 'help',
    },
    component: () => import("@/views/404"),
  },
]