<template>
  <el-config-provider :locale="locale">
    <router-view/>
  </el-config-provider>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import zh from'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'

const locale = reactive(en);
</script>