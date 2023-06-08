
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
    {
      path: "/rolePermission",
      name: "RolePermission",
      meta: {
        menu: 'system:role/permission',
        icon: 'peoples',
      },
      component: () => import("@/views/RolePermission"),
    },
    {
      path: "/license",
      name: "License",
      meta: {
        menu: 'system:license',
        icon: 'license',
      },
      component: () => import("@/views/License"),
    },
  ]
}