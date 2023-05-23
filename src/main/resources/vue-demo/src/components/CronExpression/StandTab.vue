<template>
  <div class="cron-tab-body">
    <el-row>
      <el-radio v-model="type" label="every">{{ $t('cron expression.every') }}{{ title }}</el-radio>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="cycle">{{ $t('cron expression.cycle') }}</el-radio>
      <div class="after-value">
        <span>{{ $t('cron expression.from') }}</span>&nbsp;
        <el-input-number v-model="cycleLeftNum" size="small" :min="appointArr[0]" :max="cycleRightNum - 1"
          controls-position="right"/>&nbsp;
        <span>-</span>&nbsp;
        <el-input-number v-model="cycleRightNum" size="small" :min="cycleLeftNum + 1" :max="appointArr[appointArr.length - 1]" 
          controls-position="right"/>&nbsp;
        <span>{{ title }}</span>
      </div>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="fromEvery">{{ $t('cron expression.from') }}</el-radio>
      <div class="after-value">
        <el-input-number v-model="fromEveryNum" size="small" :min="appointArr[0]" :max="appointArr[appointArr.length - 1]" 
          controls-position="right"/>&nbsp;
        <span>{{ title }}{{ $t('cron expression.start') }},{{ $t('cron expression.every') }}</span>&nbsp;
        <el-input-number v-model="fromEveryIntervalNum" size="small" :min="1" :max="appointArr[appointArr.length - 1]" 
          controls-position="right"/>&nbsp;
        <span>{{ title }}{{ $t('cron expression.execute') }}{{ $t('cron expression.once') }}</span>
      </div>
    </el-row>
    <el-row v-show="dateType == 'day'">
      <el-radio v-model="type" label="dayInMonth">{{ $t('cron expression.from') }}</el-radio>
      <div class="after-value">
        <span>{{ $t('cron expression.every') }}{{ $t('cron expression.month') }}</span>&nbsp;
        <el-input-number v-model="dayInMonth" size="small" :min="appointArr[0]" :max="appointArr[appointArr.length - 1]" 
          controls-position="right"/>&nbsp;
        <span>{{ $t('cron expression.the most recent working day') }}</span>&nbsp;
      </div>
    </el-row>
    <el-row v-show="dateType == 'day'">
      <el-radio v-model="type" label="lastDayInMonth">{{ $t('cron expression.last day of each month') }}</el-radio>
    </el-row>
    <el-row v-show="dateType == 'day' || dateType == 'month'">
      <el-radio v-model="type" label="unappoint">{{ $t('cron expression.unappoint') }}</el-radio>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="appoint">{{ $t('cron expression.appoint') }}</el-radio>
      <el-checkbox-group class="after-value" v-model="appointNumbs" style="margin-left: 20px;">
        <el-checkbox v-for="i of appointArr" :key="i" :label="i" >{{i}}</el-checkbox>
      </el-checkbox-group>
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
  dateType: String,
});

const type = computed({
  get() {
    if(props.modelValue.includes('*')) {
      return 'every';
    } else if (props.modelValue.includes('-')) {
      return 'cycle';
    } else if (props.modelValue.includes('/')) {
      return 'fromEvery';
    } else if (props.modelValue.includes('W')) {
      return 'dayInMonth';
    } else if (props.modelValue.includes('L')) {
      return 'lastDayInMonth';
    } else if (props.modelValue.includes('?')) {
      return 'unappoint';
    } else if (new RegExp('^[0-9,]+$').test(props.modelValue)) {
      return 'appoint';
    }
  },
  set(val) {
    if(val == 'every') {
      emit('update:modelValue', '*');
    } else if (val == 'cycle') {
      cycleLeftNum.value = appointArr.value[0];
      cycleRightNum.value = appointArr.value[1];
    } else if (val == 'fromEvery') {
      fromEveryNum.value = appointArr.value[0];
      fromEveryIntervalNum.value = 1;
    } else if (val == 'dayInMonth') {
      dayInMonth.value = 1;
    } else if (val == 'lastDayInMonth') {
      emit('update:modelValue', 'L');
    } else if (val == 'unappoint') {
      emit('update:modelValue', '?');
    } else if (val == 'appoint') {
      appointNumbs.value = [appointArr.value[0]];
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

const fromEveryNum = computed({
  get() {
    if(type.value == 'fromEvery') {
      return parseInt(props.modelValue.split('/')[0]);
    }
    return appointArr.value[0];
  },
  set(val) {
    emit('update:modelValue', val + '/' + fromEveryIntervalNum.value);
  }
});

const fromEveryIntervalNum = computed({
  get() {
    if(type.value == 'fromEvery') {
      return parseInt(props.modelValue.split('/')[1]);
    }
    return 1;
  },
  set(val) {
    emit('update:modelValue', fromEveryNum.value + '/' + val);
  }
});

const dayInMonth = computed({
  get() {
    if(type.value == 'dayInMonth') {
      return parseInt(props.modelValue.replace('W', ''));
    }
    return 1;
  },
  set(val) {
    emit('update:modelValue', val + 'W');
  }
});

const appointNumbs = computed({
  get() {
    if(type.value == 'appoint') {
      return props.modelValue.split(',').map(n => parseInt(n));
    }
    return [];
  },
  set(val) {
    if(val.length == 0) {
      emit('update:modelValue', [appointArr.value[0]].join(','));
    } else {
      emit('update:modelValue', val.join(','));
    }
  }
});

const appointArr = computed(() => {
  if(props.dateType == 'second' || props.dateType == 'minute') {
    return Array.from(new Array(60), (v,k) => k);
  } else if (props.dateType == 'hour') {
    return Array.from(new Array(24), (v,k) => k);
  } else if (props.dateType == 'day') {
    return Array.from(new Array(31), (v,k) => k + 1);
  } else if (props.dateType == 'month') {
    return Array.from(new Array(12), (v,k) => k + 1);
  }
});
</script>

<style lang="less" scoped>
.cron-tab-body {
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