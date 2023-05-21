
export default {
  path: "/log",
  name: "Log",
  meta: {
    menu: 'log',
    icon: 'log',
  },
  children: [
    {
      path: "/log/operator",
      name: "OperatorLog",
      meta: {
        menu: 'log:operator',
        icon: 'form',
      },
      component: () => import("@/views/Log"),
    },
    {
      path: "/log/archive",
      name: "ArchiveLog",
      meta: {
        menu: 'log:archive',
        icon: 'compress',
      },
      component: () => import("@/views/LogArchive"),
    }
  ]
}