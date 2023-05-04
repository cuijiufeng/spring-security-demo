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
import SvgIcon from '@/components/SvgIcon/index.vue'

const app = createApp(App)

// 全局组件挂载
app.component('svg-icon', SvgIcon)

// 全局方法挂载
// app.config.globalProperties.useDict = useDict

app.use(store).use(router).mount('#app')
