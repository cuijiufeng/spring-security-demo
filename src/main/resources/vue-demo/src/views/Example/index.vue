<template>
  <div class="cron-expression-body">
    <CronExpression v-model="cronExpression"/>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import CronExpression from '@/components/CronExpression';

const cronExpression = ref('0 0/1 * * * ?');
</script>

<style lang="less" scoped>
.cron-expression-body {
}
</style>