<template>
  <div class="cron-second-body">
    <el-row>
      <el-radio v-model="type" label="every">{{ $t('cron expression.every') }}{{ $t('cron expression.second') }}</el-radio>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="cycle">{{ $t('cron expression.cycle') }}</el-radio>
      <div class="after-value">
        <span>{{ $t('cron expression.from') }}</span>&nbsp;
        <el-input-number v-model="cycleLeftNum" size="small" :min="1" :max="59" 
          controls-position="right"/>&nbsp;
        <span>-</span>&nbsp;
        <el-input-number v-model="cycleRightNum" size="small" :min="1" :max="59" 
          controls-position="right"/>&nbsp;
        <span>{{ $t('cron expression.second') }}</span>
      </div>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="fromEvery">{{ $t('cron expression.from') }}</el-radio>
      <div class="after-value">
        <el-input-number size="small" :min="1" :max="58" 
          controls-position="right"/>&nbsp;
        <span>{{ $t('cron expression.second') }} {{ $t('cron expression.start') }},{{ $t('cron expression.every') }}</span>&nbsp;
        <el-input-number size="small" :min="1" :max="58" 
          controls-position="right"/>&nbsp;
        <span>{{ $t('cron expression.second') }} {{ $t('cron expression.execute') }},{{ $t('cron expression.once') }}</span>
      </div>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="appoint">{{ $t('cron expression.appoint') }}</el-radio>
      <div class="after-value" style="margin-left: 20px;">
        <el-checkbox v-for="i of 60" :key="i" :label="i" />
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
});

const type = computed({
  get() {
    if(props.modelValue.includes('*')) {
      return 'every';
    } else if (props.modelValue.includes('-')) {
      return 'cycle';
    } else if (props.modelValue.includes('/')) {
      return 'fromEvery';
    } else if (props.modelValue.includes(',')) {
      return 'appoint';
    }
  },
  set(val) {
    if(val == 'every') {
      emit('update:modelValue', '*');
    } else if (val == 'cycle') {
      emit('update:modelValue', '1-2');
    } else if (val == 'fromEvery') {
      emit('update:modelValue', '1/2');
    } else if (val == 'appoint') {
      emit('update:modelValue', '1,2');
    }
  }
})

const cycleLeftNum = computed({
  get() {
    if(type.value == 'cycle') {
      return parseInt(props.modelValue.split('-')[0]);
    }
    return 1;
  },
  set(val) {
    if(type.value == 'cycle') {
      let t = props.modelValue.split('-');
      if(parseInt(val) > t[1]) return;
      emit('update:modelValue', val + '-' + t[1]);
    }
  }
});

const cycleRightNum = computed({
  get() {
    if(type.value == 'cycle') {
      return parseInt(props.modelValue.split('-')[1]);
    }
    return 2;
  },
  set(val) {
    if(type.value == 'cycle') {
      let t = props.modelValue.split('-');
      if(parseInt(val) < t[0]) return;
      emit('update:modelValue', t[0] + '-' + val);
    }
  }
});

</script>

<style lang="less" scoped>
.cron-second-body {
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