<template>
  <div class="app-home">
    <header-view class="app-header" :sidebar-expand.sync="sidebarExpand"/>
    <div class="app-body">
      <sidebar-view :class="['app-sidebar', sidebarExpand ? 'sidebar-expand' : 'sidebar-fold']" 
        :sidebar-expand="sidebarExpand"/>
      <div class="app-main" :class="sidebarExpand ? 'sidebar-expand_main' : 'sidebar-fold_main'">
        <!-- <router-view/> -->
      </div>
    </div>
  </div>
</template>

<script>
import Header from './components/Header';
import Sidebar from './components/Sidebar';
export default {
  name: 'Home',
  components: {
    'header-view': Header,
    'sidebar-view': Sidebar,
  },
  data() {
    return {
      sidebarExpand: false
    }
  }
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
      border: 1px solid red;
    }
    .app-main {
      height: 100%;
    }
  }
}
//隐藏侧边栏的滚动条
.app-sidebar::-webkit-scrollbar {display:none}
//侧边栏收缩样式
.sidebar-expand {
  width: var(--sidebarWidth);
}
.sidebar-fold {
  width: var(--sidebarFoldWidth);
}
.sidebar-expand_main {
  width: calc(100vw - var(--sidebarWidth));
}
.sidebar-fold_main {
  width: calc(100vw - var(--sidebarFoldWidth));
}
</style>