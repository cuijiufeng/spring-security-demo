import system from './system';
import log from './log';
import job from './job';

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
  job,
  system,
  log,
]