<template>
  <div class="cron-second-body">
    <el-radio-group v-model="type">
      <el-radio label="every">{{ $t('cron expression.every second') }}</el-radio>
      <el-radio label="cycle">
        <span>{{ $t('cron expression.cycle') }}</span>&nbsp;
        <span>{{ $t('cron expression.from') }}</span>&nbsp;
        <el-input-number v-model="cycleLeftNum" size="small" :min="1" :max="58" 
          controls-position="right"/>&nbsp;
        <span>-</span>&nbsp;
        <el-input-number v-model="cycleRightNum" size="small" :min="2" :max="59" 
          controls-position="right"/>&nbsp;
        <span>秒</span>
      </el-radio>
      <el-radio label="fromEvery">
        <span>{{ $t('cron expression.from') }}</span>&nbsp;
        <el-input-number v-model="fromEveryNum" size="small" :min="1" :max="58" 
          controls-position="right"/>&nbsp;
      </el-radio>
    </el-radio-group>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';

const type = ref('every')
const cycleLeftNum = ref(1);
const cycleRightNum = ref(1);
const fromEveryNum = ref(0);
</script>

<style lang="less" scoped>
.cron-second-body {
}
</style>