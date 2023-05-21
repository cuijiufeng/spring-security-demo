import system from './system';
import log from './log';

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
  log,
]