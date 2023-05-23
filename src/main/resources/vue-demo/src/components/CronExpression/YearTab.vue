<template>
  <div class="cron-week-tab-body">
    <el-row>
      <el-radio v-model="type" label="every">{{ $t('cron expression.every') }}{{ title }}</el-radio>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="unappoint">{{ $t('cron expression.unappoint') }}</el-radio>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="cycle">{{ $t('cron expression.cycle') }}</el-radio>
      <div class="after-value">
        <span>{{ $t('cron expression.from') }}{{ title }}</span>&nbsp;
        <el-input-number v-model="cycleLeftNum" size="small" :min="appointArr[0]" :max="cycleRightNum - 1"
          controls-position="right"/>&nbsp;
        <span>-</span>&nbsp;
        <el-input-number v-model="cycleRightNum" size="small" :min="cycleLeftNum + 1" :max="appointArr[appointArr.length - 1]" 
          controls-position="right"/>&nbsp;
      </div>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, } from 'vue';

const emit = defineEmits(['update:modelValue']);

const props = defineProps({
  modelValue: {
    type: String,
    required: true,
  },
  title: String,
});

const type = computed({
  get() {
    if(props.modelValue.includes('*')) {
      return 'every';
    } else if (props.modelValue.length == 0) {
      return 'unappoint';
    } else if (props.modelValue.includes('-')) {
      return 'cycle';
    }
  },
  set(val) {
    if(val == 'every') {
      emit('update:modelValue', '*');
    } else if (val == 'unappoint') {
      emit('update:modelValue', '');
    } else if (val == 'cycle') {
      cycleLeftNum.value = appointArr.value[0];
      cycleRightNum.value = appointArr.value[1];
    }
  }
})

const cycleLeftNum = computed({
  get() {
    if(type.value == 'cycle') {
      return parseInt(props.modelValue.split('-')[0]);
    }
    return appointArr.value[0];
  },
  set(val) {
    emit('update:modelValue', val + '-' + cycleRightNum.value);
  }
});

const cycleRightNum = computed({
  get() {
    if(type.value == 'cycle') {
      return parseInt(props.modelValue.split('-')[1]);
    }
    return appointArr.value[1];
  },
  set(val) {
    emit('update:modelValue', cycleLeftNum.value + '-' + val);
  }
});

const appointArr = computed(() => {
  return Array.from(new Array(10), (v,k) => k + new Date().getFullYear());
});
</script>

<style lang="less" scoped>
.cron-week-tab-body {
  .after-value {
    font-size: 14px;
    display: flex;
    align-items: center;
    display: flex;
    flex-wrap: wrap;
  }
}

:deep(.el-radio) {
  margin-right: 15px;
}
</style>