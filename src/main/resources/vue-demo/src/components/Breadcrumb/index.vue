<template>
  <el-breadcrumb separator="/">
    <template v-for="(item, i) in breadcrumbs" :key="i"> 
      <el-breadcrumb-item v-if="item.path == '/index'" :to="item">
        {{item.title}}
      </el-breadcrumb-item>
      <el-breadcrumb-item v-esle>
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
      return this.$route.matched
      .filter(r => r.meta.title)
      .map(r => {
        return {'title': r.meta.title, 'path': r.path}
      });
    }
  }
}
</script>

<style lang="less" scoped>
</style>