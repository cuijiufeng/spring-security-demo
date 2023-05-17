<template>
  <div class="password-intensity">
    <span class="low" :style="{'background-color':intensity >=1 ? '#F56C6C' : ''}" />
    <span class="medium" :style="{'background-color':intensity >=3 ? '#E6A23C' : ''}" />
    <span class="high" :style="{'background-color':intensity >=5 ? '#67C23A' : ''}" />
  </div>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue';

const emit = defineEmits(['update:modelValue'])

const props = defineProps({
  password: {
    type: String,
    required: true
  },
  modelValue: {
    type: String,
    default: '',
  }
})

const passwordIntensity = (password) => {
  if(!password) {
    return 0;
  }
  let specialChars = "!@#\\$%\\^\\&\\*\\(\\)_\\+\\-=\\[\\]{};:,\\./<>\\?";
  let lowercase  = new RegExp('[A-Z]+');
  let uppercase = new RegExp('[a-z]+');
  let number = new RegExp('[0-9]+');
  let special = new RegExp('[' + specialChars + ']+');

  let result = [lowercase.test(password), uppercase.test(password), number.test(password), special.test(password), password.length > 8];
  return result.filter(r => r).length;
}

watch(() => props.password, (password) => {
  let intensity = passwordIntensity(password);
  if(intensity >= 5) {
    emit('update:modelValue', 'HIGH');
  } else if(intensity >= 3) {
    emit('update:modelValue', 'MEDIUM');
  } else if(intensity >= 1) {
    emit('update:modelValue', 'LOW');
  } else {
    emit('update:modelValue', '');
  }
});

const intensity = computed(() => {
  return passwordIntensity(props.password)
})
</script>

<style lang="less" scoped>
.password-intensity {
  font-size: 12px;
  line-height: 1;
  position: absolute;
  top: 100%;
}

.level(@borderColor) {
  display: inline-block;
  height: 4px;
  width: 3vw;
  margin-right: 10px;
  border-radius: 5px;
  border: 1px solid @borderColor;
}

.low {
  .level(#F56C6C);
}
.medium {
  .level(#E6A23C);
}
.high {
  .level(#67C23A);
}
</style>