<template>
  <div class="sidebar-body">
    <el-menu class="sidebar-menu" router :default-active="$route.path" :collapse="!sidebarExpand" :collapse-transition="false"
      :key="item" v-for="item in routes">
      <!-- <el-sub-menu v-if="item.meta.submenu" :index="item.path">
      </el-sub-menu> -->
      <el-menu-item v-if="!item.meta.submenu" :index="item.path">
        <el-icon><icon-menu /></el-icon>
        <span>{{item.path}}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import routes from '@/router/routes';
export default {
  name: 'Sidebar',
  components: {
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