<template>
  <div class="example-body">
    <el-tabs type="border-card">
      <el-tab-pane label="秒" name="second">
        <Second />
      </el-tab-pane>
      <el-tab-pane label="分钟" name="minute">
        <Minute />
      </el-tab-pane>
      <el-tab-pane label="小时" name="hour">
        <Hour />
      </el-tab-pane>
      <el-tab-pane label="日" name="day">
        <Day />
      </el-tab-pane>
      <el-tab-pane label="月" name="month">
        <Month />
      </el-tab-pane>
      <el-tab-pane label="星期" name="week">
        <Week />
      </el-tab-pane>
      <el-tab-pane label="年" name="year">
        <Year />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import Second from './Second';
import Minute from './Minute';
import Hour from './Hour';
import Day from './Day';
import Month from './Month';
import Week from './Week';
import Year from './Year';
</script>

<style lang="less" scoped>
.example-body {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>