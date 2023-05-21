<template>
  <el-scrollbar class="tags-scrollbar" height="auto">
    <div class="tags-container">
      <el-tag class="route-tag" v-for="tag in tags" :key="tag.path" :effect="tag.effect" 
        :closable="tag.path != '/index'" @click="$router.replace(tag.path)" @close="closeTag(tag)">
        {{ tag.title }}
      </el-tag>
    </div>
  </el-scrollbar>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router'
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';
import { ROUTE_TAGS } from '@/utils/config';

const router = useRouter();

const tags = ref(JSON.parse(sessionStorage.getItem(ROUTE_TAGS)) || []);

const closeTag = (tag) => {
  tags.value = tags.value.filter(t => tag != t);
  if(tag.effect == 'dark') {
    if(tags.value[0]) {
      router.replace(tags.value[0].path)
    } else {
      router.go(0);
    }
  }
  sessionStorage.setItem(ROUTE_TAGS, JSON.stringify(tags.value));
}

watch(useRoute(), (route) => {
  tags.value = tags.value.map(t => {
    t.effect = t.path == route.fullPath ? 'dark' : 'plain';
    return t;
  });
  if(undefined == tags.value.find(t => t.path == route.fullPath)) {
    tags.value.push({'title': route.meta.title, 'path': route.fullPath, 'effect': 'dark'});
  }
  sessionStorage.setItem(ROUTE_TAGS, JSON.stringify(tags.value));
})

onMounted(() => {
  let tag = tags.value.find(t => t.effect == 'dark');
  if(tag != undefined) {
    router.replace(tag.path);
  }
});
</script>

<style lang="less" scoped>
.tags-scrollbar {
  height: 34px;
  margin: 3px 0px 0px 3px;
  background-color: white;
  box-shadow: 5px 3px 5px #ccc;
  .tags-container {
    padding: 5px 10px 5px 10px;
    display: inline-flex;
    .route-tag {
      cursor: pointer;
      border: 1px solid #d8dce5;
      border-radius: 0px;
      margin: 0px 2px 0px 2px;
      color: #7a8495;
    }
  }
}

:deep(.el-tag .el-tag__close) {
  color: #7a8495;
  &:hover {
    background-color: #b4bccc;
  }
}
:deep(.el-tag--dark) {
  color: white !important;
  &>.el-tag__close {
    color: white;
  }
}
</style>