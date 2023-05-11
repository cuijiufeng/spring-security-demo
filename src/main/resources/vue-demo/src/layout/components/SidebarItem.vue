<template>
  <el-menu-item v-if="!route.children" :index="route.path">
    <svg-icon style="margin-right: 10px;" v-if="route.meta.icon" :icon-class="route.meta.icon"/>
    <span>{{ route.meta.title }}</span>
  </el-menu-item>
  <el-sub-menu v-else :index="route.path" :class="recursion ? 'recursion' : ''">
    <template #title>
      <svg-icon style="margin-right: 10px;" v-if="route.meta.icon" :icon-class="route.meta.icon"/>
      <span>{{ route.meta.title }}</span>
    </template>
    <sidebar-item v-for="child in route.children" :key="child.path" recursion
      :route="child" />
  </el-sub-menu>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    route: Object,
    recursion: {
      type: Boolean,
      default: false,
    },
  }
}
</script>

<style lang="less" scoped>
@height: 40px;
@fontSize: 14px;

//.el-menu
// .el-menu-item {
//   height: @height;
//   line-height: @height;
//   font-size: @fontSize;
//   &.is-active {
//     color: rgb(44, 158, 247);
//     background: rgb(233, 245, 255);
//   }
// }
// .el-sub-menu.is-opened {
//   &:not(.recursion) {
//     border-left: 3px solid rgb(44, 158, 247);
//   }
// }
// :deep(.el-sub-menu__title) {
//   height: @height;
//   line-height: @height;
//   font-size: @fontSize;
// }
</style>