<template>
  <div class="header-body">
    <div class="app-logo">
      <img class="logo-img" style="width: 100%;height: 100%;" src="@/assets/logo/logo.png">
    </div>
    <div class="header-right">
      <svg-icon icon-class="user" fill="white"/>
      <div style="width: 5px"/>
      <div style="color: white;">admin</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Header',
  components: {
  },
  data() {
    return {
    }
  },
  computed: {
    currentUser() {
      return this.$store.getters.currentUser;
    },
  },
}
</script>

<style lang="less" scoped>
.header-body {
  display: flex;
  .app-logo {
    width: var(--sidebarWidth);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .header-right {
    margin-left: auto;
    margin-right: 2vw;
    display: flex; 
    justify-content: flex-end;
    align-items: center;
  }
}
</style>