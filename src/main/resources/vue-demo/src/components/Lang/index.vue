<template>
  <div class="lang">
    <svg-icon v-if="i18n.locale.value == 'zh'" 
      icon-class="lang-en" class-name="lang-icon" @click="switchLanguage('en')"/>
    <svg-icon v-else 
      icon-class="lang-zh" class-name="lang-icon" @click="switchLanguage('zh')"/>
  </div>
</template>

<script setup>
import {useI18n } from 'vue-i18n';
const i18n = useI18n()
function switchLanguage(lang){
  i18n.locale.value = lang
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
  width: 24px;
  height: 24px;
  color: black;
}
</style>