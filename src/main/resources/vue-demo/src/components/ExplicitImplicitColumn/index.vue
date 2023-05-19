<template>
  <el-dialog :model-value="visible" :title="$t('common.explicit/implicit')" draggable
      :close-on-click-modal="false" @close="$emit('update:visible', false)" width="45%">
    <el-transfer v-model="value" :data="data" :titles="[$t('common.explicit'), $t('common.implicit')]"/>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';

const props = defineProps({
  visible: Boolean,
  modelValue: Object
});

const value = computed({
  get() {
    return Object.keys(props.modelValue)
      .filter(key => !props.modelValue[key].visible);
  },
  set(val) {
    for (const key in props.modelValue) {
      props.modelValue[key].visible = true;
    }
    props.modelValue
    val.forEach(v => props.modelValue[v].visible = false);
  }
})

const data = computed(() => {
  return Object.keys(props.modelValue).map(key => {
    return { key: key, label: props.modelValue[key].label, disabled: false };
  })
})
</script>

<style lang="less" scoped>
</style>