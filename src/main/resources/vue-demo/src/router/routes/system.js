
export default {
  path: "/system",
  name: "System",
  meta: {
    menu: 'system',
    icon: 'system',
  },
  children: [
    {
      path: "/user",
      name: "User",
      meta: {
        menu: 'system:user',
        icon: 'user',
      },
      component: () => import("@/views/User"),
    },
  ]
}