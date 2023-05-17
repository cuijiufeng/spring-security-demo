<template>
  <div class="user-body">
    <el-form :inline="true" :model="userSearch" ref="userSearchRef">
      <el-form-item :label="$t('user.username')" prop="username">
        <el-input v-model="userSearch.username" clearable :placeholder="$t('user.please input username')" />
      </el-form-item>
      <el-form-item :label="$t('user.role name')" prop="roleId">
        <el-select v-model="userSearch.roleId" clearable :placeholder="$t('user.please select role name')">
          <el-option v-for="role in roleData" :key="role.id" :label="role.roleName" :value="role.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="userList()">{{$t('common.query')}}</el-button>
        <el-button icon="Refresh" @click="$refs['userSearchRef'].resetFields();">{{$t('common.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 20px;">
      <el-button plain size="small" icon="Plus" type="primary" @click="dialogOpen.edit = true">
        {{$t('common.add')}}
      </el-button>
      <el-button plain size="small" icon="Delete" type="danger">{{$t('common.delete')}}</el-button>
      <el-button plain size="small" icon="Download" type="warning">{{$t('common.export')}}</el-button>
    </div>
    <el-table style="flex: 1;" v-loading="loading" :data="userData" :header-cell-style="{background: '#f8f8f9'}">
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" type="index" :index="index => index + 1" width="80" :label="$t('common.id')"/>
      <el-table-column align="center" show-overflow-tooltip prop="username" :label="$t('user.username')"/>
      <el-table-column align="center" show-overflow-tooltip prop="role.roleName" :label="$t('user.role name')"/>
      <el-table-column align="center" prop="passwordIntensity" :label="$t('user.password intensity')">
        <template #default="scope">
          <el-tag :type="scope.row.passwordIntensity == 'HIGH' ? 'success' : scope.row.passwordIntensity == 'LOW' ? 'danger' : ''" disable-transitions>
            {{ $t('user.' + scope.row.passwordIntensity) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="enabled" :label="$t('user.enabled')">
        <template #default="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'danger'" disable-transitions>
            {{ $t('user.' + scope.row.enabled) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" show-overflow-tooltip prop="createTime" :label="$t('user.create time')"/>
      <el-table-column align="center" show-overflow-tooltip prop="lastLoginTime" :label="$t('user.last login time')"/>
      <el-table-column align="center" :label="$t('common.operator')">
        <template #default="scope">
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Edit">{{$t('common.edit')}}</el-link>
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Delete">{{$t('common.delete')}}</el-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-model:current-page="userSearch.pageNum" v-model:page-size="userSearch.pageSize" :page-sizes="[10, 20, 50, 100]" :total="userSearch.total"
      @current-change="userList()" @size-change="userList()"/>

    <!-- 用户添加/编辑 -->
    <el-dialog v-model="dialogOpen.edit" title="新增用户" draggable :close-on-click-modal="false" 
        @close="$refs['editUserRef'].resetFields()">
      <el-form :model="editUser" ref="editUserRef" label-width="auto" inline-message :rules="{
          username: [{ required: true, message: $t('user.please input username'), trigger: 'blur' }],
          password: [{ required: true, message: $t('user.please input password'), trigger: 'blur' }],
          confirmPassword: [{ required: true, message: $t('user.please input confirm password'), trigger: 'blur' },
                            { validator: confirmValidator, trigger: 'blur' }],}">
        <el-form-item :label="$t('user.username')+':'" prop="username">
          <el-input v-model="editUser.username" maxlength="255"/>
        </el-form-item>
        <el-form-item :label="$t('user.password')+':'" prop="password">
          <el-input v-model="editUser.password" maxlength="255" type="password" show-password/>
        </el-form-item>
        <el-form-item :label="$t('user.confirm password')+':'" prop="confirmPassword">
          <el-input v-model="editUser.confirmPassword" maxlength="255" type="password" show-password/>
        </el-form-item>
        <el-form-item :label="$t('user.role name')+':'" prop="roleId">
          <el-select v-model="editUser.roleId" style="width: 100%">
            <el-option v-for="role in roleData" :key="role.id" :label="role.roleName" :value="role.id"/>
          </el-select>
        </el-form-item>
        <div style="display: flex;justify-content: center;">
          <el-button type="info" @click="dialogOpen.edit = false">{{$t('common.cancle')}}</el-button>
          <el-button type="primary" @click="handleEditUser">{{$t('common.confirm')}}</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';
import {apiUserList, apiEditUser} from '@/api/user';
import {apiRoleList} from '@/api/role';

const i18n = useI18n();

const loading = ref(false);
const dialogOpen = reactive({
  edit: false
});
const userSearch = reactive({
  pageNum: undefined,
  pageSize: undefined,
  total: undefined,
  username: '',
  roleId: '',
});
let userData = ref([]);
let roleData = ref([]);

let editUser = ref({
  confirmPassword: '',
  password: '',
  roleId: '',
  username: '',
});

const userList = () => {
  loading.value = true;
  apiUserList(userSearch).then(([data, headers]) => {
    userData.value = data.list;
    userSearch.pageNum = data.pageNum;
    userSearch.pageSize = data.pageSize;
    userSearch.total = data.total;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  }).finally(() => {
    loading.value = false;
  });
}

const editUserRef = ref();
const handleEditUser = () => {
  editUserRef.value.validate((valid) => {
    if (!valid) {
      return;
    }
    apiEditUser(editUser.value).then(([data, headers]) => {
      ElMessage({ type: 'success', message: i18n.t('common.success') });
      dialogOpen.edit = false;
      userList();
    }).catch(([data, headers]) => {
      ElMessage({ type: 'error', message: data.message });
    });
  })
}

const confirmValidator = (rule, value, callback) => {
  if (value !== editUser.value.password) {
    callback(new Error(i18n.t('user.two inputs do not match')))
  } else {
    callback()
  }
}

onMounted(() => {
  userList();
  apiRoleList({pageSize: 0, all: true}).then(([data, headers]) => {
    roleData.value = data.list;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  });
});
</script>

<style lang="less" scoped>
.user-body {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>