import { createI18n } from 'vue-i18n'; //引入vue-i18n组件
import zh from './zh/zh';
import en from './en/en';

const i18n = createI18n({
  legacy: false,
  locale: 'en',
  messages: {
    zh,
    en
  }
});

export default i18n