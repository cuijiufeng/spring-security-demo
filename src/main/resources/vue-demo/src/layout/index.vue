<template>
  <div class="app-home">
    <header-view class="app-header" v-model:sidebar-expand="sidebarExpand"/>
    <div class="app-body">
      <sidebar-view :class="['app-sidebar', sidebarExpand ? 'sidebar-expand' : 'sidebar-fold']"
        :collapse="sidebarExpand"/>
      <div class="app-main">
        <route-tags />
        <div class="app-container">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import RouteTags from '@/components/RouteTags';
import { SIDEBAR_EXPAND } from '@/utils/config';
export default {
  name: 'Home',
  components: {
    'header-view': Header,
    'sidebar-view': Sidebar,
    'route-tags': RouteTags,
  },
  data() {
    return {
      sidebarExpand: localStorage.getItem(SIDEBAR_EXPAND) 
        ? !!+localStorage.getItem(SIDEBAR_EXPAND)
        : true,
    }
  },
}
</script>

<style lang="less" scoped>
.app-home {
  width: 100vw;
  height: 100vh;
  background: var(--appBg);
  .app-header {
    width: 100vw;
    height: var(--headerHeight);
    background-color: var(--headerBg);
    box-shadow: 5px 3px 5px #ccc;
  }
  .app-body{
    width: 100vw;
    height: calc(100vh - var(--headerHeight));
    display: flex;
    .app-sidebar {
      height: 100%;
    }
    .app-main {
      height: 100%;
      flex: 1;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      .app-container {
        background: white;
        flex: 1;
        margin-left: 3px;
        overflow: hidden;
      }
    }
  }
}
//侧边栏收缩样式
.sidebar-expand {
  width: var(--sidebarWidth);
}
.sidebar-fold {
  width: var(--sidebarFoldWidth);
}
</style>