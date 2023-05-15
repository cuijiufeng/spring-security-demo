<template>
  <el-breadcrumb separator="/">
    <template v-for="(item, i) in breadcrumbs" :key="i">
      <el-breadcrumb-item v-if="item.path == '/index'" :to="item">
        {{item.title}}
      </el-breadcrumb-item>
      <el-breadcrumb-item v-else>
        {{item.title}}
      </el-breadcrumb-item>
    </template>
  </el-breadcrumb>
</template>

<script>
export default {
  name: 'Breadcrumb',
  computed: {
    breadcrumbs() {
      let breadcrumbs = this.$route.matched
        .filter(r => r.meta.title)
        .map(r => {
          return {'title': r.meta.title, 'path': r.path}
        });
      if(undefined == breadcrumbs.find(r => r.path == '/index')) {
        breadcrumbs.unshift({'title': this.$t('layout.index'), 'path': '/index'});
      }
      return breadcrumbs;
    }
  }
}
</script>

<style lang="less" scoped>
</style>