<template>
  <div class="header-body">
    <div :class="['header-logo', sidebarExpand ? 'header-logo-expand' : 'header-logo-fold']">
      <img v-if="sidebarExpand" class="logo-img" style="width: 80%;height: 100%;" src="@/assets/logo/logo.png">
      <img v-else class="logo-img" style="width: 80%;height: 100%;" src="@/assets/logo/logo-mini.png">
    </div>
    <div class="header-center">
      <el-icon style="cursor: pointer;" size="20" @click="toggleSidebar">
        <Fold v-if="sidebarExpand"/>
        <Expand v-else/>
      </el-icon>
      <Breadcrumb style="margin-left: 20px;"/>
    </div>
    <div class="header-right">
      <language/>
      <el-dropdown>
        <div class="dropdown-user-link">
          <svg-icon icon-class="user"/>
          <div style="width: 5px"/>
          <span>{{userStore.currentUser.username}}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <!-- <el-dropdown-item icon="User">{{$t('layout.personal center')}}</el-dropdown-item> -->
            <el-dropdown-item icon="Key" @click="dialogVisit = true">{{$t('layout.change password')}}</el-dropdown-item>
            <el-dropdown-item divided icon="SwitchButton" @click="logout">{{$t('layout.logout')}}</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <!-- 修改密码 -->
    <el-dialog v-model="dialogVisit" :title="$t('layout.modify password')" width="40%"
        draggable :close-on-click-modal="false" @close="$refs['passwordFormRef'].resetFields()">
      <el-form :model="passwordForm" ref="passwordFormRef" label-width="auto" inline-message 
        style="width: 70%;" :rules="{
          originalPassword: [{ required: true, message: $t('user.please input original password'), trigger: 'blur' }],
          password: [{ required: true, message: $t('user.please input password'), trigger: 'blur' },
                      { min: 8, message: $t('user.length too short'), trigger: 'blur' }],
          confirmPassword: [{ validator: confirmValidator, trigger: 'blur' }]
        }">
        <el-form-item :label="$t('user.original password')+':'" prop="originalPassword">
          <el-input v-model="passwordForm.originalPassword" maxlength="255" type="password" show-password/>
        </el-form-item>
        <el-form-item :label="$t('user.password')+':'" prop="password">
          <el-input v-model="passwordForm.password" maxlength="255" type="password" show-password/>
          <PasswordIntensity v-model="passwordForm.passwordIntensity" :password="passwordForm.password"/>
        </el-form-item>
        <el-form-item :label="$t('user.confirm password')+':'" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" maxlength="255" type="password" show-password/>
        </el-form-item>
        <div style="display: flex;justify-content: center;">
          <el-button type="info" @click="dialogVisit = false">{{$t('common.cancel')}}</el-button>
          <el-button type="primary" @click="handleModifyPassword">{{$t('common.confirm')}}</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import useUserStore from '@/store/modules/user';
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';
import Breadcrumb from '@/components/Breadcrumb';
import PasswordIntensity from '@/components/PasswordIntensity';
import { apiLogout } from '@/api/system';
import { apiEditUser } from '@/api/user';
import { SIDEBAR_EXPAND } from '@/utils/config';

const i18n = useI18n();

const emit = defineEmits(['update:sidebar-expand'])

const userStore = useUserStore();

const props = defineProps({
  sidebarExpand: Boolean,
});

const dialogVisit = ref(false);
const passwordForm = ref({
  id: userStore.currentUser.id,
  username: userStore.currentUser.username,
  originalPassword: '',
  password: '',
  confirmPassword: '',
  passwordIntensity: '',
  accountNonExpired: userStore.currentUser.accountNonExpired,
  accountNonLocked: userStore.currentUser.accountNonLocked,
  credentialsNonExpired: userStore.currentUser.credentialsNonExpired,
  enabled: userStore.currentUser.enabled,
});

const confirmValidator = (rule, value, callback) => {
  if (value !== passwordForm.value.password) {
    callback(new Error(i18n.t('user.two inputs do not match')))
  } else {
    callback()
  }
}

const passwordFormRef = ref();

const handleModifyPassword = () => {
  passwordFormRef.value.validate((valid) => {
    if (!valid) {
      return;
    }
    apiEditUser(passwordForm.value).then(([data, headers]) => {
      ElMessage({ type: 'success', message: i18n.t('common.success') });
      location.reload();
    }).catch(([data, headers]) => {
      ElMessage({ type: 'error', message: data.message });
    });
  })
}

const logout = () => {
  apiLogout({username: userStore.currentUser.username}).then(([data, headers]) => {
    userStore.logout();
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  });
}
const toggleSidebar = () => {
  localStorage.setItem(SIDEBAR_EXPAND, !props.sidebarExpand ? 1 : 0);
  emit('update:sidebar-expand', !props.sidebarExpand);
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