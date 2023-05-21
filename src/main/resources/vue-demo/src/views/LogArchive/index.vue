<template>
  <div class="log-archive-body">
    <el-form v-show="visibleSearch" inline :model="logArchiveSearch" ref="logArchiveSearchRef">
      <el-form-item :label="$t('log.operator time')" prop="dateRange">
        <el-date-picker v-model="logArchiveSearch.dateRange" type="daterange" clearable 
          range-separator="-" value-format="YYYY-MM-DD HH:mm:ss"
          :start-placeholder="$t('common.start date')" :end-placeholder="$t('common.end date')"/>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="logArchiveList">{{$t('common.query')}}</el-button>
        <el-button icon="Refresh" @click="$refs['logArchiveSearchRef'].resetFields();">{{$t('common.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 10px;">
      <div style="float: right;">
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit search')" placement="top">
          <el-button circle icon="Search" @click="visibleSearch = !visibleSearch"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.refresh')" placement="top">
          <el-button circle icon="Refresh" @click="logArchiveList"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit column')" placement="top">
          <el-button circle icon="Menu" @click="dialogOpen.visibleExplicitImplicitColumn = true"/>
        </el-tooltip>
      </div>
    </div>
    <el-table style="flex: 1;" v-loading="loading" :data="logArchiveListData" :header-cell-style="{background: '#f8f8f9'}">
      <el-table-column type="selection" width="55" />
      <el-table-column v-if="explicitImplicitColumn.no.visible" align="center" type="index" 
        :index="index => index + 1" width="80" :label="$t('common.no')"/>
      <el-table-column v-if="explicitImplicitColumn.fileName.visible" align="center" show-overflow-tooltip prop="fileName" 
        :label="$t('log archive.file name')"/>
      <el-table-column v-if="explicitImplicitColumn.fileSize.visible" align="center" show-overflow-tooltip prop="fileSize" 
        :label="$t('log archive.file size')">
        <template #default="scope">
          {{ fileSizeConvert(scope.row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.archiveTime.visible" align="center" show-overflow-tooltip prop="archiveTime" 
        :label="$t('log archive.archive time')"/>
      <el-table-column v-if="explicitImplicitColumn.archiveCnt.visible" align="center" show-overflow-tooltip prop="archiveCnt" 
        :label="$t('log archive.archive count')"/>
      <el-table-column v-if="explicitImplicitColumn.operator.visible" align="center" :label="$t('common.operator')">
        <template #default="scope">
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Download" @click="downloadLogs(scope.row.id)">
            {{$t('common.download')}}
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-model:current-page="logArchiveSearch.pageNum" v-model:page-size="logArchiveSearch.pageSize" :page-sizes="[10, 20, 50, 100]" :total="logArchiveSearch.total"
      @current-change="logArchiveList" @size-change="logArchiveList"/>

    <ExplicitImplicitColumn v-model="explicitImplicitColumn" v-model:visible="dialogOpen.visibleExplicitImplicitColumn"/>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useI18n } from "vue-i18n";
import { saveAs } from 'file-saver';
import { ElMessage, ElMessageBox } from 'element-plus';
import { apiLogArchiveList, apiDownloadLogArchive } from '@/api/logArchive';

const i18n = useI18n();

const sizeUnit = reactive(["B", "KB", "MB", "GB", "TB"]);
const loading = ref(false);
const visibleSearch = ref(true);
const dialogOpen = reactive({
  visibleExplicitImplicitColumn: false,
});
const explicitImplicitColumn = reactive({
  no: { label: i18n.t('common.no'), visible: true },
  fileName: { label: i18n.t('log archive.file name'), visible: true },
  fileSize: { label: i18n.t('log archive.file size'), visible: true },
  archiveTime: { label: i18n.t('log archive.archive time'), visible: true },
  archiveCnt: { label: i18n.t('log archive.archive count'), visible: true },
  operator: { label: i18n.t('common.operator'), visible: true },
});
const logArchiveSearch = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  dateRange: [],
});

let logArchiveListData = ref([]);

const logArchiveList = () => {
  loading.value = true;
  apiLogArchiveList(logArchiveSearch).then(([data, headers]) => {
    logArchiveListData.value = data.list;
    logArchiveSearch.pageNum = data.pageNum;
    logArchiveSearch.pageSize = data.pageSize;
    logArchiveSearch.total = data.total;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  }).finally(() => {
    loading.value = false;
  });
}

const downloadLogs = (id) => {
  apiDownloadLogArchive({id: id}).then(([data, headers]) => {
    saveAs(new Blob([data]), headers['content-disposition'])
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  });
}

const fileSizeConvert = (size) => {
  if(!size) {
    return '';
  }
  let t = size;
  let decimal = 0;
  let forCnt = 0;
  while (Math.trunc(t / 1024) != 0) {
    decimal = t % 1024;
    t = Math.trunc(t / 1024);
    forCnt++;
  }
  return t + "." + decimal + sizeUnit[forCnt];
}

onMounted(() => {
  logArchiveList();
});
</script>

<style lang="less" scoped>
.log-archive-body {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>