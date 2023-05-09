<template>
  <div class="header-body">
    <div :class="['header-logo', sidebarExpand ? 'header-logo-expand' : 'header-logo-fold']">
      <img class="logo-img" style="width: 80%;height: 100%;" src="@/assets/logo/logo.png">
    </div>
    <div class="header-center">
      <el-icon size="20" @click="$emit('update:sidebarExpand', !sidebarExpand)">
        <Fold v-if="sidebarExpand"/>
        <Expand v-else/>
      </el-icon>
    </div>
    <div class="header-right">
      <language/>
      <el-dropdown popper-class="dropdown-user">
        <div class="dropdown-user-link">
          <svg-icon icon-class="user" class-name="user-icon"/>
          <div style="width: 6px"/>
          <div style="color: black;size: 18px;">{{currentUser.username}}</div>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item icon="User">{{$t('layout.personal center')}}</el-dropdown-item>
            <el-dropdown-item icon="Key">{{$t('layout.change password')}}</el-dropdown-item>
            <el-dropdown-item divided icon="SwitchButton" @click="logout">{{$t('layout.logout')}}</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { logout } from '@/api/system'
export default {
  name: 'Header',
  components: {
  },
  props: {
    sidebarExpand: Boolean,
  },
  methods: {
    logout() {
      logout({username: this.currentUser.username}).then(([data, headers]) => {
        this.$store.commit('logout');
      }).catch(([err, headers]) => {
        this.$message({ type: 'error', message: err.message, });
      });
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
  .header-logo {
    display: flex;
    justify-content: center;
    &-expand {
      width: var(--sidebarWidth);
    }
    &-fold {
      width: var(--sidebarFoldWidth);
    }
  }
  .header-center {
    display: flex;
    align-items: center;
    margin-left: 1vw;
  }
  .header-right {
    margin-left: auto;
    margin-right: 2vw;
    display: flex; 
  }
}
.user-icon {
  width: 18px;
  height: 18px;
  color: black;
}
//el-dropdown
:global(.dropdown-user .el-dropdown-menu__item:not(.is-disabled):focus) {
  color: black;
  background-color: #f0f3f4;
}
.dropdown-user-link {
  cursor: pointer;
  color: var(--headerBg);
  display: flex;
  align-items: center;
  outline: none;
}
</style>