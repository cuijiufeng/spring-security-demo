<template>
  <div class="login-body">
    <canvas id="login-canvas" style="width: 100%;height: 100%"/>
  </div>
</template>

<script>
import "@/assets/js/canvas"
import { onMounted } from 'vue';
export default {
  name: 'Login',
  components: {
  },
  props: {
  },
  data() {
    return {
    }
  },
  setup() {
    onMounted(() =>  {
      debugger
    })
  }
}
</script>

<style lang="less" scoped>
.login-body {
  width: 100vw;
  height: 100vh;
}
</style>