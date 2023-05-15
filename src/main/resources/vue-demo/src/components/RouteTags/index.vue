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

<script>
import { ROUTE_TAGS } from '@/utils/config';
export default {
  name: 'RouteTags',
  data() {
    return {
      tags: JSON.parse(sessionStorage.getItem(ROUTE_TAGS)) 
        || [{'title': this.$t('layout.index'), 'path': '/index', 'effect': 'plain'}],
    }
  },
  methods: {
    closeTag(tag) {
      this.tags = this.tags.filter(t => tag != t);
      if(tag.effect == 'dark') {
        this.$router.replace(this.tags[0].path)
      }
      this.cacheRouteTags();
    },
    cacheRouteTags() {
      sessionStorage.setItem(ROUTE_TAGS, JSON.stringify(this.tags));
    }
  },
  watch: {
    $route: [ 
      function changedRoute(route) {
        this.tags.forEach(t => {
          t.effect = 'plain';
        });
        let tag = this.tags.find(t => t.path == route.fullPath);
        if(undefined == tag) {
          this.tags.push({'title': route.meta.title, 'path': route.fullPath, 'effect': 'dark'});
        } else {
          tag.effect = 'dark';
        }
        this.dark = route.fullPath;
        this.cacheRouteTags();
      }
    ]
  }
}
</script>

<style lang="less" scoped>
.tags-scrollbar{
  height: auto;
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