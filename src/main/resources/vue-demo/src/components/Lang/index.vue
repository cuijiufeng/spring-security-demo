<template>
  <el-dropdown trigger="hover" @command="switchLanguage">
    <div class="lang">
      <svg-icon icon-class="lang"/>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item :disabled="i18n.locale.value === 'zh'" command="zh">中文</el-dropdown-item>
        <el-dropdown-item :disabled="i18n.locale.value === 'en'" command="en">English</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
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
  font-size: 16px;
}
</style>