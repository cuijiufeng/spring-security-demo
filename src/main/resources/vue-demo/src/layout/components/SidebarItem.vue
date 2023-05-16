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

<script setup>
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';

const props = defineProps({
  route: Object,
  rootNode: {
    type: Boolean,
    default: true,
  },
  collapse: Boolean
});
</script>

<style lang="less" scoped>
.svg-icon {
  flex-shrink: 0;
  margin-right: 16px;
}
.el-menu--collapse .svg-icon {
  margin-right: 0px;
}
// 文字溢出显示...
.menu-title {
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}


//.el-menu样式
.el-menu-item {
  &.is-active {
    color: rgb(44, 158, 247);
    background: rgb(233, 245, 255);
  }
  &:hover {
    background-color: rgba(0, 0, 0, 0.06) !important;
  }
}
.el-sub-menu {
  &.is-opened.rootNode{
    border-left: 3px solid rgb(44, 158, 247);
  }
  :deep(.el-sub-menu__title) {
    &:hover {
      background-color: rgba(0, 0, 0, 0.06) !important;
    }
  }
}

.el-menu--collapse {
  .el-menu-item {
    justify-content: center;
  }
}
// TODO
.el-menu--collapse.el-sub-menu__title {
  justify-content: center;
}
</style>