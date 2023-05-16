<template>
  <svg :class="svgClass" aria-hidden="true">
    <use :xlink:href="iconName" :fill="color"/>
  </svg>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';

const props = defineProps({
  iconClass: {
    type: String,
    required: true
  },
  className: {
    type: String
  },
  color: {
    type: String,
    default: ''
  }
})

const iconName = computed(() => {
  return `#icon-${props.iconClass}`;
})
const svgClass = computed(() => {
  return props.className ? 'svg-icon ' + props.className : 'svg-icon';
})
</script>

<style scope lang="less">
.svg-icon {
  width: 1em;
  height: 1em;
  position: relative;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>
