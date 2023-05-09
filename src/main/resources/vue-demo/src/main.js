import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//引css样式
import "@/assets/styles/global.less";
import "@/assets/styles/reset.css";
import "@/assets/styles/scrollbar.less";
//svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
//element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//i18n
import i18n from '@/i18n/index'
import Lang from '@/components/Lang'
//elementui icons
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 全局组件挂载
app.component('svg-icon', SvgIcon)
app.component('language', Lang)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 全局方法挂载
// app.config.globalProperties.useDict = useDict

//应用启动
store.dispatch("appCreate");

app.use(store)
  .use(router)
  .use(ElementPlus)
  .use(i18n)
  .mount('#app')