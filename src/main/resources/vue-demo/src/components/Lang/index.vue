<template>
  <div class="lang">
    <svg-icon v-if="i18n.locale.value == 'en'" 
      icon-class="lang-en" class-name="lang-icon" @click="switchLanguage('zh')"/>
    <svg-icon v-else 
      icon-class="lang-zh" class-name="lang-icon" @click="switchLanguage('en')"/>
  </div>
</template>

<script setup>
import { LANGUAGE } from '@/utils/config';
import { useI18n } from 'vue-i18n';
const i18n = useI18n();
//初始化
i18n.locale.value = localStorage.getItem(LANGUAGE) || 'zh';

function switchLanguage(lang) {
  i18n.locale.value = lang;
  localStorage.setItem(LANGUAGE, lang);
}
</script>

<style lang="less" scoped>
.lang {
  width: 60px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}
.lang-icon {
  width: 22px;
  height: 22px;
  color: black;
}
</style>