<template>
  <div class="sidebar-body">
    <el-menu class="sidebar-menu" router :default-active="$route.path" :collapse="!sidebarExpand" :collapse-transition="false">
      <sidebar-item :route="item" :key="item" v-for="item in routes"/>
    </el-menu>
  </div>
</template>

<script>
import routes from '@/router/routes';
import SidebarItem from './SidebarItem';
export default {
  name: 'Sidebar',
  components: {
    "sidebar-item": SidebarItem
  },
  props: {
    sidebarExpand: Boolean,
  },
  data() {
    return {
    }
  },
  computed: {
    routes() {
      return routes;
    }
  }
}
</script>

<style lang="less" scoped>
.sidebar-body {
  box-shadow: 3px 5px 5px #ccc;
  .sidebar-menu {
    margin-top: 3px;
    height: 100%;
    width: 100%;
  }
}
</style>