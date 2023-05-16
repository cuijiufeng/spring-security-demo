<template>
  <div class="sidebar-body">
    <el-scrollbar>
      <el-menu router 
        :default-active="this.$route.path" 
        mode="vertical"
        :collapse="!collapse" 
        :collapse-transition="false"
        text-color="#7a8495">
        <sidebar-item :route="item" :key="item.path" v-for="item in routes" :collapse="!collapse"/>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import {dynamicRoute} from '@/router';
import SidebarItem from './SidebarItem';
export default {
  name: 'Sidebar',
  components: {
    "sidebar-item": SidebarItem
  },
  props: {
    collapse: Boolean,
  },
  computed: {
    routes() {
      return dynamicRoute.children;
    },
  }
}
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