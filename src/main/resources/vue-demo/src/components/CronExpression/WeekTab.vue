<template>
  <div class="cron-week-tab-body">
    <el-row>
      <el-radio v-model="type" label="every">{{ $t('cron expression.every') }}{{ title }}</el-radio>
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
    <el-row>
      <el-radio v-model="type" label="weekInMonth">{{}}</el-radio>
      <div class="after-value">
        <span>{{ $t('cron expression.the') }}</span>&nbsp;
        <el-input-number v-model="weekInMonthNum" size="small" :min="1" :max="4"
          controls-position="right"/>&nbsp;
        <span>{{ title }}{{ $t('cron expression.of') }}{{title}}</span>&nbsp;
        <el-input-number v-model="weekNum" size="small" :min="appointArr[0]" :max="appointArr[appointArr.length - 1]"
          controls-position="right"/>&nbsp;
      </div>
    </el-row>
    <el-row>
      <el-radio v-model="type" label="lastWeekInMonth">{{}}</el-radio>
      <div class="after-value">
        <span>{{ $t('cron expression.last week of this month') }}</span>&nbsp;
        <el-input-number v-model="lastWeekInMonth" size="small" :min="appointArr[0]" :max="appointArr[appointArr.length - 1]"
          controls-position="right"/>&nbsp;
      </div>
    </el-row>
    <el-row>
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
});

const type = computed({
  get() {
    if(props.modelValue.includes('*')) {
      return 'every';
    } else if (props.modelValue.includes('/')) {
      return 'cycle';
    } else if (props.modelValue.includes('#')) {
      return 'weekInMonth';
    } else if (props.modelValue.includes('L')) {
      return 'lastWeekInMonth';
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
    } else if (val == 'weekInMonth') {
      weekInMonthNum.value = 1;
      weekNum.value = appointArr.value[0];
    } else if (val == 'lastWeekInMonth') {
      lastWeekInMonth.value = appointArr.value[0];
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
      return parseInt(props.modelValue.split('/')[0]);
    }
    return appointArr.value[0];
  },
  set(val) {
    emit('update:modelValue', val + '/' + cycleRightNum.value);
  }
});

const cycleRightNum = computed({
  get() {
    if(type.value == 'cycle') {
      return parseInt(props.modelValue.split('/')[1]);
    }
    return appointArr.value[1];
  },
  set(val) {
    emit('update:modelValue', cycleLeftNum.value + '/' + val);
  }
});

const weekInMonthNum = computed({
  get() {
    if(type.value == 'weekInMonth') {
      return parseInt(props.modelValue.split('#')[0]);
    }
    return 1;
  },
  set(val) {
    emit('update:modelValue', val + '#' + weekNum.value);
  }
});

const weekNum = computed({
  get() {
    if(type.value == 'weekInMonth') {
      return parseInt(props.modelValue.split('#')[1]);
    }
    return appointArr.value[0];
  },
  set(val) {
    emit('update:modelValue', weekInMonthNum.value + '#' + val);
  }
});

const lastWeekInMonth = computed({
  get() {
    if(type.value == 'lastWeekInMonth') {
      return parseInt(props.modelValue.replace('L', ''));
    }
    return appointArr.value[0];
  },
  set(val) {
    emit('update:modelValue', val + 'L');
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
  return Array.from(new Array(7), (v,k) => k + 1);
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