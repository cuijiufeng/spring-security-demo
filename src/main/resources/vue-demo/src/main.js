import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//引css样式
import "@/assets/styles/global.less";
import "@/assets/styles/reset.css";
import "@/assets/styles/elementui.less";
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
// 分页组件
import Pagination from '@/components/Pagination'
import ExplicitImplicitColumn from '@/components/ExplicitImplicitColumn';

const app = createApp(App)

// 全局组件挂载
app.component('svg-icon', SvgIcon)
app.component('language', Lang)
app.component('pagination', Pagination)
app.component('ExplicitImplicitColumn', ExplicitImplicitColumn)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 全局方法挂载
// app.config.globalProperties.downloadFile = saveAs;

app.use(store)
  .use(router)
  .use(ElementPlus)
  .use(i18n)
  .mount('#app')