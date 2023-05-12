<template>
  <el-tooltip v-if="rootNode && collapse" effect="dark" :content="route.meta.title" placement="right">
    <el-menu-item v-if="!route.children" :index="route.path">
      <svg-icon v-if="route.meta.icon" :icon-class="route.meta.icon"/>
      <span class="menu-title">{{ route.meta.title }}</span>
    </el-menu-item>
  </el-tooltip>
  <template v-else>
    <el-menu-item v-if="!route.children" :index="route.path">
      <svg-icon v-if="route.meta.icon" :icon-class="route.meta.icon"/>
      <span class="menu-title">{{ route.meta.title }}</span>
    </el-menu-item>
  </template>
  <el-sub-menu v-if="route.children" :index="route.path" :class="rootNode ? 'rootNode' : ''">
    <template #title>
      <svg-icon v-if="route.meta.icon" :icon-class="route.meta.icon"/>
      <span class="menu-title">{{ route.meta.title }}</span>
    </template>
    <sidebar-item v-for="child in route.children" :key="child.path" :rootNode="false"
      :route="child" :collapse="collapse"/>
  </el-sub-menu>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    route: Object,
    rootNode: {
      type: Boolean,
      default: true,
    },
    collapse: Boolean
  }
}
</script>

<style lang="less" scoped>
.svg-icon {
  flex-shrink: 0;
  margin-right: 16px;
}
// 文字溢出显示...
.menu-title {
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}
</style>