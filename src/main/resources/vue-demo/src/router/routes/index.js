
export default [
  {
    path: "/index",
    name: "Index",
    meta: {
      menu: 'index',
      icon: 'user',
    },
    component: () => import("@/views/Example"),
  }, 
  {
    path: "/help",
    name: "Help",
    meta: {
      menu: 'help',
      icon: 'user',
    },
    component: () => import("@/views/Example"),
  },
  {
    path: "/group",
    name: "Group",
    meta: {
      menu: 'group',
      icon: '',
    },
    children: [
      {
        path: "/group/one",
        name: "GroupOne",
        meta: {
          menu: 'one',
          icon: '',
        },
        children: [
          {
            path: "/group/one/one",
            name: "GroupOneOne",
            meta: {
              menu: 'one',
              icon: '',
            },
            component: () => import("@/views/Example"),
          },
        ]
      },
      {
        path: "/group/two",
        name: "GroupTwo",
        meta: {
          menu: 'two',
          icon: '',
        },
        component: () => import("@/views/Example"),
      },
    ]
  },
]