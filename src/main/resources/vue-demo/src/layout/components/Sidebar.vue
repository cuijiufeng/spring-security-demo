<template>
  <div class="sidebar-body">
    <el-scrollbar>
      <el-menu router 
        :default-active="this.$route.path" 
        mode="vertical"
        :collapse="!collapse" 
        :collapse-transition="false"
        text-color="#7a8495">
        <SidebarItem :route="item" :key="item.path" v-for="item in routes" :collapse="!collapse"/>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';
import {dynamicRoute} from '@/router';
import SidebarItem from './SidebarItem';

const props = defineProps({
  collapse: Boolean,
});

const routes = computed(() => {
  return dynamicRoute.children;
})
</script>

<style lang="less" scoped>
.sidebar-body {
  box-shadow: 3px 5px 5px #ccc;
  margin-top: 3px;
  background: #fff;
}

.el-menu {
  border: none;
  height: 100%;
  width: 100%;
}
</style>