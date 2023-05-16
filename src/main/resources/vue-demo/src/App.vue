<template>
  <el-config-provider :locale="locale">
    <router-view/>
  </el-config-provider>
</template>

<script>
import zh from'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
export default {
  name: "App",
  data() {
    return {
      locale: en
    }
  }
}
</script>