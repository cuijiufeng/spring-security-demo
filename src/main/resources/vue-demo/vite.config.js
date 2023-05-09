import path from 'path'
import {defineConfig} from 'vite'

import vue from '@vitejs/plugin-vue'
import {createSvgIconsPlugin} from 'vite-plugin-svg-icons';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    //svg-icon导入
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/svg')],
      symbolId: 'icon-[dir]-[name]',
    })
  ],
  resolve: {
    alias: {
      // 设置路径
      '~': path.resolve(__dirname, './'),
      // 设置别名
      '@': path.resolve(__dirname, './src'),
      'vue-i18n': 'vue-i18n/dist/vue-i18n.cjs.js',
    },
    //import from省略后缀
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
  },
  // vite 相关配置
  server: {
    host: "0.0.0.0",
    port: 3000,
    // 是否自动在浏览器打开
    open: false,
    // 是否开启 https
    https: false,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  }
})
