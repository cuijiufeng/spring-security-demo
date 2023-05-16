<template>
  <el-breadcrumb separator="/">
    <template v-for="(item, i) in breadcrumbs" :key="i">
      <el-breadcrumb-item v-if="item.path == '/index'" :to="item">
        {{item.title}}
      </el-breadcrumb-item>
      <el-breadcrumb-item v-else>
        {{item.title}}
      </el-breadcrumb-item>
    </template>
  </el-breadcrumb>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRoute } from 'vue-router'
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';

const breadcrumbs = computed(() => {
  let breadcrumbs = useRoute().matched
    .filter(r => r.meta.title)
    .map(r => {
      return {'title': r.meta.title, 'path': r.path}
    });
  if(undefined == breadcrumbs.find(r => r.path == '/index')) {
    breadcrumbs.unshift({'title': useI18n().t('layout.index'), 'path': '/index'});
  }
  return breadcrumbs;
})
</script>

<style lang="less" scoped>
</style>