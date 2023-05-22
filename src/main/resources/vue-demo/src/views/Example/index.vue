<template>
  <div class="cron-expression-body">
    <CronExpression />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import CronExpression from '@/components/CronExpression';
</script>

<style lang="less" scoped>
.cron-expression-body {
}
</style>