//引入vue-i18n组件
import {createI18n} from 'vue-i18n';
import {LANGUAGE} from '@/utils/config';
import zh from './zh/zh';
import en from './en/en';

const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: localStorage.getItem(LANGUAGE) || 'zh',
  messages: {
    zh,
    en
  }
});

export default i18n