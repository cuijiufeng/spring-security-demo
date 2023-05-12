<template>
  <div class="header-body">
    <div :class="['header-logo', sidebarExpand ? 'header-logo-expand' : 'header-logo-fold']">
      <img class="logo-img" style="width: 80%;height: 100%;" src="@/assets/logo/logo.png">
    </div>
    <div class="header-center">
      <el-icon style="cursor: pointer;" size="20" @click="toggleSidebar">
        <Fold v-if="sidebarExpand"/>
        <Expand v-else/>
      </el-icon>
      <breadcrumb style="margin-left: 20px;"/>
    </div>
    <div class="header-right">
      <language/>
      <el-dropdown>
        <div class="dropdown-user-link">
          <svg-icon icon-class="user"/>
          <div style="width: 5px"/>
          <span>{{currentUser.username}}</span>
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
import Breadcrumb from '@/components/Breadcrumb';
import { logout } from '@/api/system';
import { SIDEBAR_EXPAND } from '@/utils/config';
export default {
  name: 'Header',
  components: {
    'breadcrumb': Breadcrumb,
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
    },
    toggleSidebar() {
      localStorage.setItem(SIDEBAR_EXPAND, !this.sidebarExpand ? 1 : 0);
      this.$emit('update:sidebar-expand', !this.sidebarExpand);
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
.dropdown-user-link {
  cursor: pointer;
  font-size: 16px;
  color: #7a8495;
  display: flex;
  align-items: center;
}
</style>