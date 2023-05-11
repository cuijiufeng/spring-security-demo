
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
  {
    path: "/help",
    name: "Help",
    meta: {
      menu: 'help',
      icon: 'home',
    },
    component: () => import("@/views/Example"),
  },
  {
    path: "/group",
    name: "Group",
    meta: {
      menu: 'group',
      icon: 'home',
    },
    children: [
      {
        path: "/group/one",
        name: "GroupOne",
        meta: {
          menu: 'group:one',
          icon: 'home',
        },
        children: [
          {
            path: "/group/one/one",
            name: "GroupOneOne",
            meta: {
              menu: 'group:one:one',
              icon: 'home',
            },
            component: () => import("@/views/Example"),
          },
        ]
      },
      {
        path: "/group/two",
        name: "GroupTwo",
        meta: {
          menu: 'group:two',
          icon: 'home',
        },
        component: () => import("@/views/Example"),
      },
    ]
  },
  {
    path: "/group1",
    name: "Group1",
    meta: {
      menu: 'group',
      icon: 'home',
    },
    children: [
      {
        path: "/group/one1",
        name: "GroupOne1",
        meta: {
          menu: 'group:one',
          icon: 'home',
        },
        children: [
          {
            path: "/group/one/one1",
            name: "GroupOneOne1",
            meta: {
              menu: 'group:one:one',
              icon: 'home',
            },
            component: () => import("@/views/Example"),
          },
        ]
      },
      {
        path: "/group/two1",
        name: "GroupTwo1",
        meta: {
          menu: 'group:two',
          icon: 'home',
        },
        component: () => import("@/views/Example"),
      },
    ]
  },
  {
    path: "/group2",
    name: "Group2",
    meta: {
      menu: 'group',
      icon: 'home',
    },
    children: [
      {
        path: "/group/one2",
        name: "GroupOne2",
        meta: {
          menu: 'group:one',
          icon: 'home',
        },
        children: [
          {
            path: "/group/one/one2",
            name: "GroupOneOne2",
            meta: {
              menu: 'group:one:one',
              icon: 'home',
            },
            component: () => import("@/views/Example"),
          },
        ]
      },
      {
        path: "/group/two2",
        name: "GroupTwo2",
        meta: {
          menu: 'group:two',
          icon: 'home',
        },
        component: () => import("@/views/Example"),
      },
    ]
  },
  {
    path: "/group3",
    name: "Group3",
    meta: {
      menu: 'group',
      icon: 'home',
    },
    children: [
      {
        path: "/group/one3",
        name: "GroupOne3",
        meta: {
          menu: 'group:one',
          icon: 'home',
        },
        children: [
          {
            path: "/group/one/one3",
            name: "GroupOneOne3",
            meta: {
              menu: 'group:one:one',
              icon: 'home',
            },
            component: () => import("@/views/Example"),
          },
        ]
      },
      {
        path: "/group/two3",
        name: "GroupTwo3",
        meta: {
          menu: 'group:two',
          icon: 'home',
        },
        component: () => import("@/views/Example"),
      },
    ]
  },
  {
    path: "/group4",
    name: "Group4",
    meta: {
      menu: 'group',
      icon: 'home',
    },
    children: [
      {
        path: "/group/one4",
        name: "GroupOne4",
        meta: {
          menu: 'group:one',
          icon: 'home',
        },
        children: [
          {
            path: "/group/one/one4",
            name: "GroupOneOne4",
            meta: {
              menu: 'group:one:one',
              icon: 'home',
            },
            component: () => import("@/views/Example"),
          },
        ]
      },
      {
        path: "/group/two4",
        name: "GroupTwo4",
        meta: {
          menu: 'group:two',
          icon: 'home',
        },
        component: () => import("@/views/Example"),
      },
    ]
  },
  {
    path: "/group5",
    name: "Group5",
    meta: {
      menu: 'group',
      icon: 'home',
    },
    children: [
      {
        path: "/group/one5",
        name: "GroupOne5",
        meta: {
          menu: 'group:one',
          icon: 'home',
        },
        children: [
          {
            path: "/group/one/one5",
            name: "GroupOneOne5",
            meta: {
              menu: 'group:one:one',
              icon: 'home',
            },
            component: () => import("@/views/Example"),
          },
        ]
      },
      {
        path: "/group/two5",
        name: "GroupTwo5",
        meta: {
          menu: 'group:two',
          icon: 'home',
        },
        component: () => import("@/views/Example"),
      },
    ]
  },
]