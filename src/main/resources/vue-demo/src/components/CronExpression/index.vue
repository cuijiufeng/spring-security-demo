<template>
  <div class="example-body" style="width: 50vw;">
    <!-- <el-form-item label="cron:">
      <el-input :model-value="modelValue" disabled />
    </el-form-item> -->
    <el-tabs v-model="activeName" type="border-card">
      <el-tab-pane :label="$t('cron expression.second')" name="second">
        <StandTab v-model="second" :title="$t('cron expression.second')" dateType="second"/>
      </el-tab-pane>
      <el-tab-pane :label="$t('cron expression.minute')" name="minute">
        <StandTab v-model="minute" :title="$t('cron expression.minute')" dateType="minute"/>
      </el-tab-pane>
      <el-tab-pane :label="$t('cron expression.hour')" name="hour">
        <StandTab v-model="hour" :title="$t('cron expression.hour')" dateType="hour"/>
      </el-tab-pane>
      <el-tab-pane :label="$t('cron expression.day')" name="day">
        <StandTab v-model="day" :title="$t('cron expression.day')" dateType="day"/>
      </el-tab-pane>
      <el-tab-pane :label="$t('cron expression.month')" name="month">
        <StandTab v-model="month" :title="$t('cron expression.month')" dateType="month"/>
      </el-tab-pane>
      <el-tab-pane :label="$t('cron expression.week')" name="week">
        <WeekTab v-model="week" :title="$t('cron expression.week')"/>
      </el-tab-pane>
      <el-tab-pane :label="$t('cron expression.year')" name="year">
        <YearTab v-model="year" :title="$t('cron expression.year')"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { ElMessage } from 'element-plus';
import StandTab from './StandTab';
import WeekTab from './WeekTab';
import YearTab from './YearTab';

const emit = defineEmits(['update:modelValue']);

const props = defineProps({
  modelValue: {
    type: String,
    required: true,
    default: '* * * * * ?',
  },
});

const activeName = ref('second')

const second = computed({
  get() {
    if(!props.modelValue) {
      debugger
    }
    return props.modelValue.split(' ')[0];
  },
  set(val) {
    let arr = [...props.modelValue.split(' ')];
    arr[0] = val;
    emit('update:modelValue', arr.join(' '));
  }
});
const minute = computed({
  get() {
    return props.modelValue.split(' ')[1];
  },
  set(val) {
    let arr = [...props.modelValue.split(' ')];
    arr[1] = val;
    emit('update:modelValue', arr.join(' '));
  }
});
const hour = computed({
  get() {
    return props.modelValue.split(' ')[2];
  },
  set(val) {
    let arr = [...props.modelValue.split(' ')];
    arr[2] = val;
    emit('update:modelValue', arr.join(' '));
  }
});
const day = computed({
  get() {
    return props.modelValue.split(' ')[3];
  },
  set(val) {
    let arr = [...props.modelValue.split(' ')];
    arr[3] = val;
    emit('update:modelValue', arr.join(' '));
  }
});
const month = computed({
  get() {
    return props.modelValue.split(' ')[4];
  },
  set(val) {
    let arr = [...props.modelValue.split(' ')];
    arr[4] = val;
    emit('update:modelValue', arr.join(' '));
  }
});
const week = computed({
  get() {
    return props.modelValue.split(' ')[5];
  },
  set(val) {
    let arr = [...props.modelValue.split(' ')];
    arr[5] = val;
    emit('update:modelValue', arr.join(' '));
  }
});
const year = computed({
  get() {
    return props.modelValue.split(' ')[6] ? props.modelValue.split(' ')[6] : '';
  },
  set(val) {
    let arr = [...props.modelValue.split(' ')];
    arr[6] = val;
    emit('update:modelValue', arr.join(' ').trim());
  }
});
</script>

<style lang="less" scoped>
.example-body {
}
</style>