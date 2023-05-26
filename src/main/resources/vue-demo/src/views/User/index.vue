<template>
  <div class="user-body">
    <el-form v-show="visibleSearch" :inline="true" :model="userSearch" ref="userSearchRef">
      <el-form-item :label="$t('user.username')" prop="username">
        <el-input v-model="userSearch.username" clearable :placeholder="$t('user.please input username')" />
      </el-form-item>
      <el-form-item :label="$t('user.role name')" prop="roleId">
        <el-select v-model="userSearch.roleId" clearable :placeholder="$t('user.please select role name')">
          <el-option v-for="role in roleListData" :key="role.id" :label="role.roleName" :value="role.id"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('user.sex')" prop="sex">
        <el-select v-model="userSearch.sex" clearable>
          <el-option :label="$t('user.MALE')" value="MALE"/>
          <el-option :label="$t('user.FEMALE')" value="FEMALE"/>
          <el-option :label="$t('user.UNKNOWN')" value="UNKNOWN"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('user.phone number')" prop="phoneNumber">
        <el-input v-model="userSearch.phoneNumber" clearable :placeholder="$t('user.please input number type')" />
      </el-form-item>
      <el-form-item :label="$t('user.create time')" prop="dateRange">
        <el-date-picker v-model="userSearch.dateRange" type="daterange" clearable 
          range-separator="-" value-format="YYYY-MM-DD HH:mm:ss"
          :start-placeholder="$t('common.start date')" :end-placeholder="$t('common.end date')"/>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="userList()">{{$t('common.query')}}</el-button>
        <el-button icon="Refresh" @click="$refs['userSearchRef'].resetFields();">{{$t('common.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 10px;">
      <el-button plain size="small" icon="Plus" type="primary" @click="openAddUser">
        {{$t('common.add')}}
      </el-button>
      <el-button plain size="small" icon="Delete" type="danger" @click="deleteUser(multiSelectUsers.map(u => u.id))">
        {{$t('common.delete')}}
      </el-button>
      <!-- <el-button plain size="small" icon="Upload" type="info">{{$t('common.import')}}</el-button> -->
      <el-button plain size="small" icon="Download" type="warning" @click="userExport">{{$t('common.export')}}</el-button>
      <div style="float: right;">
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit search')" placement="top">
          <el-button circle icon="Search" @click="visibleSearch = !visibleSearch"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.refresh')" placement="top">
          <el-button circle icon="Refresh" @click="userList"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit column')" placement="top">
          <el-button circle icon="Menu" @click="dialogOpen.visibleExplicitImplicitColumn = true"/>
        </el-tooltip>
      </div>
    </div>
    <el-table style="flex: 1;" v-loading="loading" :data="userListData" :header-cell-style="{background: '#f8f8f9'}"
        @selection-change="(val) => multiSelectUsers = val">
      <el-table-column type="selection" width="55" />
      <el-table-column v-if="explicitImplicitColumn.no.visible" align="center" type="index" 
        :index="index => index + 1" width="80" :label="$t('common.no')"/>
      <el-table-column v-if="explicitImplicitColumn.username.visible" align="center" show-overflow-tooltip prop="username" 
        :label="$t('user.username')"/>
      <el-table-column v-if="explicitImplicitColumn.sex.visible" align="center" prop="sex" :label="$t('user.sex')">
        <template #default="scope">
          <span v-if="!scope.row.sex"/>
          <el-tag v-else :type="scope.row.sex == 'MALE' ? '' : scope.row.sex == 'FEMALE' ? 'danger' : 'info'" 
              disable-transitions>
            {{ $t('user.' + scope.row.sex) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.phoneNumber.visible" align="center" prop="phoneNumber" 
        :label="$t('user.phone number')"/>
      <el-table-column v-if="explicitImplicitColumn.roleName.visible" align="center" show-overflow-tooltip prop="role.roleName" 
        :label="$t('user.role name')"/>
      <el-table-column v-if="explicitImplicitColumn.passwordIntensity.visible" align="center" prop="passwordIntensity" 
        :label="$t('user.password intensity')">
        <template #default="scope">
          <span v-if="!scope.row.passwordIntensity"/>
          <el-tag v-else :type="scope.row.passwordIntensity == 'HIGH' ? 'success' : scope.row.passwordIntensity == 'LOW' ? 'danger' : ''" 
              disable-transitions>
            {{ $t('user.' + scope.row.passwordIntensity) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.enabled.visible" align="center" prop="enabled" :label="$t('user.enabled')">
        <template #default="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'danger'" disable-transitions>
            {{ $t('user.' + scope.row.enabled) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.createTime.visible" align="center" show-overflow-tooltip prop="createTime" 
        :label="$t('user.create time')"/>
      <el-table-column v-if="explicitImplicitColumn.lastLoginTime.visible" align="center" show-overflow-tooltip 
        prop="lastLoginTime" :label="$t('user.last login time')"/>
      <el-table-column v-if="explicitImplicitColumn.operator.visible" align="center" :label="$t('common.operator')">
        <template #default="scope">
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Edit" @click="openEditUser(scope.row)">
            {{$t('common.edit')}}
          </el-link>
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Delete" @click="deleteUser([scope.row.id])">
            {{$t('common.delete')}}
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-model:current-page="userSearch.pageNum" v-model:page-size="userSearch.pageSize" :page-sizes="[10, 20, 50, 100]" :total="userSearch.total"
      @current-change="userList()" @size-change="userList()"/>

    <!-- 用户添加/编辑 -->
    <el-dialog v-model="dialogOpen.visibleAddEdit" :title="$t('user.add/edit user', {type: dialogOpen.isEdit ? $t('common.edit') : $t('common.add')})" 
        draggable :close-on-click-modal="false" @close="editUser = {}">
      <el-form :model="editUser" ref="editUserRef" label-width="auto" inline-message :rules="{
          username: [{ required: true, message: $t('user.please input username'), trigger: 'blur' }],
          phoneNumber: [{ validator: phoneNumberValidator, trigger: 'blur' }],
          originalPassword: [{ required: !dialogOpen.isEdit, message: $t('user.please input original password'), trigger: 'blur' }],
          password: [{ required: !dialogOpen.isEdit, message: $t('user.please input password'), trigger: 'blur' },
                    { min: 8, message: $t('user.length too short'), trigger: 'blur' }],
          confirmPassword: [{ validator: confirmValidator, trigger: 'blur' }],}">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('user.username')+':'" prop="username">
              <el-input v-model="editUser.username" maxlength="255" :disabled="dialogOpen.isEdit"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('user.sex')+':'" prop="sex">
              <el-select v-model="editUser.sex" style="width: 100%">
                <el-option :label="$t('user.MALE')" value="MALE"/>
                <el-option :label="$t('user.FEMALE')" value="FEMALE"/>
                <el-option :label="$t('user.UNKNOWN')" value="UNKNOWN"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('user.phone number')+':'" prop="phoneNumber">
              <el-input v-model.number="editUser.phoneNumber" maxlength="11"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item v-if="dialogOpen.isEdit" :label="$t('user.original password')+':'" prop="originalPassword">
              <el-input v-model="editUser.originalPassword" maxlength="255" type="password" show-password/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('user.password')+':'" prop="password">
              <el-input v-model="editUser.password" maxlength="255" type="password" show-password/>
              <PasswordIntensity v-model="editUser.passwordIntensity" :password="editUser.password"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('user.confirm password')+':'" prop="confirmPassword">
              <el-input v-model="editUser.confirmPassword" maxlength="255" type="password" show-password/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('user.role name')+':'" prop="roleId">
              <el-select v-model="editUser.roleId" style="width: 100%" clearable>
                <el-option v-for="role in roleListData" :key="role.id" :label="role.roleName" :value="role.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('user.enabled')+':'" prop="enabled">
              <el-radio-group v-model="editUser.enabled">
                <el-radio :label="true">{{ $t('user.true') }}</el-radio>
                <el-radio :label="false">{{ $t('user.false') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <div style="display: flex;justify-content: center;">
          <el-button type="info" @click="dialogOpen.visibleAddEdit = false">{{$t('common.cancel')}}</el-button>
          <el-button type="primary" @click="handleEditUser">{{$t('common.confirm')}}</el-button>
        </div>
      </el-form>
    </el-dialog>
    <ExplicitImplicitColumn v-model="explicitImplicitColumn" v-model:visible="dialogOpen.visibleExplicitImplicitColumn"/>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useI18n } from "vue-i18n";
import { ElMessage, ElMessageBox } from 'element-plus';
import { saveAs } from 'file-saver';
import PasswordIntensity from '@/components/PasswordIntensity';
import {apiUserList, apiEditUser, apiDeleteUser, apiUserExport} from '@/api/user';
import {apiRoleList} from '@/api/role';

const i18n = useI18n();

const loading = ref(false);
const visibleSearch = ref(true);
const explicitImplicitColumn = reactive({
  no: { label: i18n.t('common.no'), visible: true },
  username: { label: i18n.t('user.username'), visible: true },
  sex: { label: i18n.t('user.sex'), visible: true },
  phoneNumber: { label: i18n.t('user.phone number'), visible: true },
  roleName: { label: i18n.t('user.role name'), visible: true },
  passwordIntensity: { label: i18n.t('user.password intensity'), visible: true },
  enabled: { label: i18n.t('user.enabled'), visible: true },
  createTime: { label: i18n.t('user.create time'), visible: true },
  lastLoginTime: { label: i18n.t('user.last login time'), visible: false },
  operator: { label: i18n.t('common.operator'), visible: true },
});
const dialogOpen = reactive({
  visibleExplicitImplicitColumn: false,
  visibleAddEdit: false,
  isEdit: false,
});
const userSearch = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  dateRange: [],
  username: '',
  roleId: '',
  sex: '',
  phoneNumber: '',
});
let userListData = ref([]);
let roleListData = ref([]);

let editUser = ref({});
let multiSelectUsers = ref([]);

const userList = () => {
  loading.value = true;
  apiUserList(userSearch).then(([data, headers]) => {
    userListData.value = data.list;
    userSearch.pageNum = data.pageNum;
    userSearch.pageSize = data.pageSize;
    userSearch.total = data.total;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  }).finally(() => {
    loading.value = false;
  });
}

const openAddUser = () => {
  dialogOpen.visibleAddEdit = true;
  dialogOpen.isEdit = false;
  editUser.value.enabled = true;
}
const openEditUser = (user) => {
  dialogOpen.visibleAddEdit = true;
  dialogOpen.isEdit = true;
  editUser.value = JSON.parse(JSON.stringify(user));
}

const editUserRef = ref();
const handleEditUser = () => {
  editUserRef.value.validate((valid) => {
    if (!valid) {
      return;
    }
    apiEditUser(editUser.value).then(([data, headers]) => {
      ElMessage({ type: 'success', message: i18n.t('common.success') });
      dialogOpen.visibleAddEdit = false;
      userList();
    }).catch(([data, headers]) => {
      ElMessage({ type: 'error', message: data.message });
    });
  })
}

const deleteUser = (ids) => {
  if(!ids || ids.length == 0) {
    ElMessage({ type: 'warning', message: i18n.t('common.no items to delete') });
    return;
  }
  ElMessageBox.confirm(
    i18n.t('common.unrecoverable after deletion'),
    i18n.t('common.warning'),
    {
      confirmButtonText: i18n.t('common.confirm'),
      cancelButtonText: i18n.t('common.cancel'),
      type: 'warning',
      draggable: true,
    }
  ).then(() => {
    apiDeleteUser({ids: ids}).then(([data, headers]) => {
      ElMessage({ type: 'success', message: i18n.t('common.success') });
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

const phoneNumberValidator = (rule, value, callback) => {
  let reg = new RegExp('^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$');
  if (value && !reg.test(value)) {
    callback(new Error(i18n.t('user.phone number format error')))
  } else {
    callback()
  }
}

const userExport = () => {
  apiUserExport({ids: multiSelectUsers.value.map(l => l.id)}).then(([data, headers]) => {
    saveAs(new Blob([data]), headers['content-disposition'])
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  })
}

onMounted(() => {
  userList();
  apiRoleList({pageSize: 0, all: true}).then(([data, headers]) => {
    roleListData.value = data.list;
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