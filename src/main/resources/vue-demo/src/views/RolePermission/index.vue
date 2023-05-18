<template>
  <div class="role-permission-body">
    <el-form v-show="visibleSearch" inline :model="roleSearch" ref="roleSearchRef">
      <el-form-item :label="$t('role.role name')" prop="roleName">
        <el-input v-model="roleSearch.roleName" clearable :placeholder="$t('role.please input role name')" />
      </el-form-item>
      <el-form-item :label="$t('role.role key')" prop="roleKey">
        <el-input v-model="roleSearch.roleKey" clearable :placeholder="$t('role.please input role key')" />
      </el-form-item>
      <el-form-item :label="$t('role.create time')" prop="dateRange">
        <el-date-picker v-model="roleSearch.dateRange" type="daterange" clearable 
          range-separator="-" value-format="YYYY-MM-DD HH:mm:ss"
          :start-placeholder="$t('common.start date')" :end-placeholder="$t('common.end date')"/>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="roleList">{{$t('common.query')}}</el-button>
        <el-button icon="Refresh" @click="$refs['roleSearchRef'].resetFields();">{{$t('common.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 10px;">
      <el-button plain size="small" icon="Plus" type="primary">
        {{$t('common.add')}}
      </el-button>
      <el-button plain size="small" icon="Delete" type="danger">
        {{$t('common.delete')}}
      </el-button>
      <div style="float: right;">
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit search')" placement="top">
          <el-button circle icon="Search" @click="visibleSearch = !visibleSearch"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.refresh')" placement="top">
          <el-button circle icon="Refresh" @click="roleList"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit column')" placement="top">
          <el-button circle icon="Menu" @click="dialogOpen.visibleExplicitImplicitColumn = true"/>
        </el-tooltip>
      </div>
    </div>
    <el-table style="flex: 1;" v-loading="loading" :data="roleListData" :header-cell-style="{background: '#f8f8f9'}"
        @selection-change="(val) => multiSelectRoles = val">
      <el-table-column type="selection" width="55" />
      <el-table-column v-if="explicitImplicitColumn.no.visible" align="center" type="index" 
        :index="index => index + 1" width="80" :label="$t('common.no')"/>
      <el-table-column v-if="explicitImplicitColumn.roleName.visible" align="center" show-overflow-tooltip prop="roleName" 
        :label="$t('role.role name')"/>
      <el-table-column v-if="explicitImplicitColumn.roleKey.visible" align="center" show-overflow-tooltip prop="roleKey" 
        :label="$t('role.role key')"/>
      <el-table-column v-if="explicitImplicitColumn.level.visible" align="center" show-overflow-tooltip prop="level" 
        :label="$t('role.role level')"/>
      <el-table-column v-if="explicitImplicitColumn.createTime.visible" align="center" show-overflow-tooltip prop="createTime" 
        :label="$t('role.create time')"/>
      <el-table-column v-if="explicitImplicitColumn.operator.visible" align="center" :label="$t('common.operator')">
        <template #default="scope">
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Edit">
            {{$t('common.edit')}}
          </el-link>
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Delete">
            {{$t('common.delete')}}
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-model:current-page="roleSearch.pageNum" v-model:page-size="roleSearch.pageSize" :page-sizes="[10, 20, 50, 100]" :total="roleSearch.total"
      @current-change="roleList" @size-change="roleList"/>

    <ExplicitImplicitColumn v-model="explicitImplicitColumn" v-model:visible="dialogOpen.visibleExplicitImplicitColumn"/>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useI18n } from "vue-i18n";
import { ElMessage, ElMessageBox } from 'element-plus';
import {apiRoleList} from '@/api/role';

const i18n = useI18n();

const loading = ref(false);
const visibleSearch = ref(true);
const dialogOpen = reactive({
  visibleExplicitImplicitColumn: false,
  visibleAddEdit: false,
  isEdit: false,
});
const roleSearch = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  dateRange: [],
  roleName: '',
  roleKey: '',
});
const explicitImplicitColumn = reactive({
  no: { label: i18n.t('common.no'), visible: true },
  roleName: { label: i18n.t('role.role name'), visible: true },
  roleKey: { label: i18n.t('role.role key'), visible: true },
  level: { label: i18n.t('role.role level'), visible: true },
  createTime: { label: i18n.t('role.create time'), visible: true },
  operator: { label: i18n.t('common.operator'), visible: true },
});

let roleListData = ref([]);
let editRole = ref({});
let multiSelectRoles = ref([]);

const roleList = () => {
  loading.value = true;
  apiRoleList(roleSearch).then(([data, headers]) => {
    roleListData.value = data.list;
    roleSearch.pageNum = data.pageNum;
    roleSearch.pageSize = data.pageSize;
    roleSearch.total = data.total;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  }).finally(() => {
    loading.value = false;
  });
}

onMounted(() => {
  roleList();
});
</script>

<style lang="less" scoped>
.role-permission-body {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>