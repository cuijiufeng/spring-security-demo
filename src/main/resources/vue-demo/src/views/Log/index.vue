<template>
  <div class="log-body">
    <el-form v-show="visibleSearch" inline :model="logSearch" ref="logSearchRef">
      <el-form-item :label="$t('log.operator user')" prop="optUser">
        <el-input v-model="logSearch.optUser" clearable :placeholder="$t('log.please input operator user')" />
      </el-form-item>
      <el-form-item :label="$t('log.operator status')" prop="resultStatus">
        <el-select v-model="logSearch.resultStatus" clearable>
          <el-option :label="$t('common.success')" :value="true"/>
          <el-option :label="$t('common.failed')" :value="false"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('log.audit status')" prop="audited">
        <el-select v-model="logSearch.audited" clearable>
          <el-option :label="$t('common.success')" :value="true"/>
          <el-option :label="$t('common.failed')" :value="false"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('log.operator time')" prop="dateRange">
        <el-date-picker v-model="logSearch.dateRange" type="daterange" clearable 
          range-separator="-" value-format="YYYY-MM-DD HH:mm:ss"
          :start-placeholder="$t('common.start date')" :end-placeholder="$t('common.end date')"/>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="logList">{{$t('common.query')}}</el-button>
        <el-button icon="Refresh" @click="$refs['logSearchRef'].resetFields();">{{$t('common.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 10px;">
      <el-button plain size="small" icon="Collection" type="primary" @click="logCompressArchive">{{$t('log.compress archive')}}</el-button>
      <el-button plain size="small" icon="Download" type="warning">{{$t('common.export')}}</el-button>
      <div style="float: right;">
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit search')" placement="top">
          <el-button circle icon="Search" @click="visibleSearch = !visibleSearch"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.refresh')" placement="top">
          <el-button circle icon="Refresh" @click="logList"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit column')" placement="top">
          <el-button circle icon="Menu" @click="dialogOpen.visibleExplicitImplicitColumn = true"/>
        </el-tooltip>
      </div>
    </div>
    <el-table style="flex: 1;" v-loading="loading" :data="logListData" :header-cell-style="{background: '#f8f8f9'}"
        @selection-change="(val) => multiSelectLogs = val">
      <el-table-column type="selection" width="55" />
      <el-table-column v-if="explicitImplicitColumn.no.visible" align="center" type="index" 
        :index="index => index + 1" width="80" :label="$t('common.no')"/>
      <el-table-column v-if="explicitImplicitColumn.optDesc.visible" align="center" show-overflow-tooltip prop="optDesc" 
        :label="$t('log.operator description')"/>
      <el-table-column v-if="explicitImplicitColumn.optUser.visible" align="center" show-overflow-tooltip prop="optUser" 
        :label="$t('log.operator user')"/>
      <el-table-column v-if="explicitImplicitColumn.resultCode.visible" align="center" show-overflow-tooltip prop="resultCode" 
        :label="$t('log.operator status')">
        <template #default="scope">
          <el-tag :type="scope.row.resultCode == 200 ? '' : 'danger'" disable-transitions>
            {{ scope.row.resultCode == 200 ? $t('common.success') : $t('common.failed') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.errCode.visible" align="center" show-overflow-tooltip prop="errCode" 
        :label="$t('log.error code')"/>
      <el-table-column v-if="explicitImplicitColumn.errMsg.visible" align="center" show-overflow-tooltip prop="errMsg" 
        :label="$t('log.error message')"/>
      <el-table-column v-if="explicitImplicitColumn.costTime.visible" align="center" show-overflow-tooltip prop="costTime" 
        :label="$t('log.cost time')">
        <template #default="scope">
          {{ scope.row.costTime }} {{ $t('common.millisecond') }}
        </template>  
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.optTime.visible" align="center" show-overflow-tooltip prop="optTime" 
        :label="$t('log.operator time')"/>
      <el-table-column v-if="explicitImplicitColumn.audited.visible" align="center" show-overflow-tooltip prop="audited" 
        :label="$t('log.audit status')">
        <template #default="scope">
          <span v-if="scope.row.audited === undefined"></span>
          <el-tag v-else :type="scope.row.audited? '' : 'danger'" disable-transitions>
            {{ scope.row.audited ? $t('common.success') : $t('common.failed') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.mac.visible" align="center" show-overflow-tooltip prop="mac" 
        label="Mac"/>
      <el-table-column v-if="explicitImplicitColumn.operator.visible" align="center" :label="$t('common.operator')">
        <template #default="scope">
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Finished" @click="auditLog(scope.row.id)">
            {{$t('log.audit')}}
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-model:current-page="logSearch.pageNum" v-model:page-size="logSearch.pageSize" :page-sizes="[10, 20, 50, 100]" :total="logSearch.total"
      @current-change="logList" @size-change="logList"/>

    <ExplicitImplicitColumn v-model="explicitImplicitColumn" v-model:visible="dialogOpen.visibleExplicitImplicitColumn"/>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useI18n } from "vue-i18n";
import { ElMessage, ElMessageBox } from 'element-plus';
import { apiLogList, apiAuditLog, apiCompressArchive } from '@/api/log';

const i18n = useI18n();

const loading = ref(false);
const visibleSearch = ref(true);
const dialogOpen = reactive({
  visibleExplicitImplicitColumn: false,
});
const explicitImplicitColumn = reactive({
  no: { label: i18n.t('common.no'), visible: true },
  optDesc: { label: i18n.t('log.operator description'), visible: true },
  optUser: { label: i18n.t('log.operator user'), visible: true },
  resultCode: { label: i18n.t('log.operator status'), visible: true },
  errCode: { label: i18n.t('log.error code'), visible: false },
  errMsg: { label: i18n.t('log.error message'), visible: false },
  costTime: { label: i18n.t('log.cost time'), visible: true },
  optTime: { label: i18n.t('log.operator time'), visible: true },
  audited: { label: i18n.t('log.audit status'), visible: true },
  mac: { label: 'Mac', visible: false },
  operator: { label: i18n.t('common.operator'), visible: true },
});
const logSearch = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  dateRange: [],
  optUser: '',
  resultStatus: '',
  audited: '',
});

let logListData = ref([]);
let multiSelectLogs = ref([]);

const logList = () => {
  loading.value = true;
  apiLogList(logSearch).then(([data, headers]) => {
    logListData.value = data.list;
    logSearch.pageNum = data.pageNum;
    logSearch.pageSize = data.pageSize;
    logSearch.total = data.total;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  }).finally(() => {
    loading.value = false;
  });
}

const auditLog = (id) => {
  apiAuditLog({id: id}).then(([data, headers]) => {
    ElMessage(data ? i18n.t('common.success') : i18n.t('common.failed'));
    logList();
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  })
}

const logCompressArchive = () => {
  apiCompressArchive({ids: multiSelectLogs.value.map(l => l.id)}).then(([data, headers]) => {
    ElMessage(i18n.t('common.success'));
    logList();
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  })
}

onMounted(() => {
  logList();
});
</script>

<style lang="less" scoped>
.log-body {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>