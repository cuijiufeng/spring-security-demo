<template>
  <div class="sidebar-item">
    <el-menu-item v-if="!route.children" :index="route.path">
      <svg-icon v-if="route.meta.icon" :icon-class="route.meta.icon" 
        style="width: 20px;height: 20px;margin-right: 10px;"/>
      <span style="color: #7a8495;">{{ route.meta.title }}</span>
    </el-menu-item>
    <el-sub-menu v-else :index="route.path">
      <template #title>
        <svg-icon v-if="route.meta.icon" :icon-class="route.meta.icon" 
          style="width: 20px;height: 20px;margin-right: 10px;"/>
        <span style="color: #7a8495;">{{ route.meta.title }}</span>
      </template>
      <sidebar-item v-for="child in route.children" :key="child"
        :route="child" />
    </el-sub-menu>
  </div>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    route: Object
  }
}
</script>

<style lang="less" scoped>
.sidebar-item {
  overflow: hidden;
}
</style>