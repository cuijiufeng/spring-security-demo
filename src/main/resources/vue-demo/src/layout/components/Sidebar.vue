<template>
  <div class="sidebar-body">
    <el-menu default-active="2" :collapse="sidebarExpand">
    
    </el-menu>
  </div>
</template>

<script>
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
  },
}
</script>

<style lang="less" scoped>

</style>