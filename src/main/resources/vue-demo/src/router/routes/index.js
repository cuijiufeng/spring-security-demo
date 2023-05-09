
export default [
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