<template>
  <div class="pagination-body">
    <el-pagination background layout="total, sizes, prev, pager, next, jumper" 
      @update:current-page="(val) => $emit('update:current-page', val)" 
      @update:page-size="(val) => $emit('update:page-size', val)" :page-sizes="pageSizes" :total="total" 
      @current-change="$emit('current-change')" @size-change="$emit('size-change')"/>
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    pageSizes: Array,
    total: Number,
  },
}
</script>

<style lang="less" scoped>
.pagination-body {
  margin-top: 20px;
}
</style>