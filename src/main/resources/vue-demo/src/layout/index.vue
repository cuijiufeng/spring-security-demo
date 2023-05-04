<template>
  <div class="app-home">
    <header-view class="app-header"/>
    <div class="app-body">
      <!-- <router-view/> -->
    </div>
  </div>
</template>

<script>
import Header from './components/Header.vue';
export default {
  name: 'Home',
  components: {
    'header-view': Header,
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
    box-shadow: 5px 3px 5px #888888;
  }
  .app-body{
    width: 100vw;
    height: calc(100vh - var(--headerHeight));
  }
}
//隐藏侧边栏的滚动条
.app-sidebar::-webkit-scrollbar {display:none}
//侧边栏收缩样式
.open {
  &-sidebar {
    width: var(--sidebarWidth);
  }
  &-sidebar-main {
    width: calc(100vw - var(--sidebarWidth));
  }
}
.close {
  &-sidebar {
    width: var(--sidebarWidthClose);
  }
  &-sidebar-main {
      width: calc(100vw - var(--sidebarWidthClose));
  }
}
</style>