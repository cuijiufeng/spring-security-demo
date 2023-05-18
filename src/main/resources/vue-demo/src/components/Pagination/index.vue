<template>
  <div class="pagination-body">
    <el-pagination background small layout="total, sizes, prev, pager, next, jumper" 
      @update:current-page="(val) => $emit('update:current-page', val)" 
      @update:page-size="(val) => $emit('update:page-size', val)" :page-sizes="pageSizes" :total="total" 
      @current-change="$emit('current-change')" @size-change="$emit('size-change')"/>
  </div>
</template>

<script setup>
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';

const props = defineProps({
  pageSizes: { 
    type: Array
  },
  total: {
    type: Number
  },
})
</script>

<style lang="less" scoped>
.pagination-body {
  margin-top: 20px;
}
</style>