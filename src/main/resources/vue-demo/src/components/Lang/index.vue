<template>
  <el-dropdown trigger="hover" @command="switchLanguage">
    <div class="lang">
      <svg-icon icon-class="lang" :color="color"/>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item :disabled="locale === 'zh'" command="zh">中文</el-dropdown-item>
        <el-dropdown-item :disabled="locale === 'en'" command="en">English</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { LANGUAGE } from '@/utils/config';
import { useI18n } from 'vue-i18n';
const { locale } = useI18n()

const props = defineProps({
  color: {
    type: String,
    default: ''
  }
})

function switchLanguage(lang) {
  locale.value = lang;
  localStorage.setItem(LANGUAGE, lang);
  location.reload();
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