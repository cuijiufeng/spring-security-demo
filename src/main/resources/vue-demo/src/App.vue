<template>
  <el-config-provider :locale="elLocale">
    <router-view/>
  </el-config-provider>
</template>

<script setup>
import { ref, reactive,watch } from 'vue';
import useAppStore from '@/store/modules/app';
import { useI18n } from 'vue-i18n';
import zh from'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'

const { locale } = useI18n();
const elLocale = ref(locale.value == 'zh' ? zh : en);

watch(locale, (val) => {
  elLocale.value = val == 'zh' ? zh : en;
})

//应用启动
useAppStore().appCreate();
</script>