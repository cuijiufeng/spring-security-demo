<template>
  <div class="job-body">
    <el-form v-show="visibleSearch" inline :model="jobSearch" ref="jobSearchRef">
      <el-form-item :label="$t('job.job name')" prop="jobName">
        <el-input v-model="jobSearch.jobName" clearable :placeholder="$t('job.please input job name')" />
      </el-form-item>
      <el-form-item :label="$t('job.start status')" prop="started">
        <el-select v-model="jobSearch.started" clearable>
          <el-option :label="$t('job.start')" :value="true"/>
          <el-option :label="$t('job.stop')" :value="false"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="jobList">{{$t('common.query')}}</el-button>
        <el-button icon="Refresh" @click="$refs['jobSearchRef'].resetFields();">{{$t('common.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 10px;">
      <div style="float: right;">
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit search')" placement="top">
          <el-button circle icon="Search" @click="visibleSearch = !visibleSearch"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.refresh')" placement="top">
          <el-button circle icon="Refresh" @click="jobList"/>
        </el-tooltip>
        <el-tooltip class="box-item" effect="dark" :content="$t('common.explicit implicit column')" placement="top">
          <el-button circle icon="Menu" @click="dialogOpen.visibleExplicitImplicitColumn = true"/>
        </el-tooltip>
      </div>
    </div>
    <el-table style="flex: 1;" v-loading="loading" :data="jobListData" :header-cell-style="{background: '#f8f8f9'}">
      <el-table-column type="selection" width="55" />
      <el-table-column v-if="explicitImplicitColumn.no.visible" align="center" type="index" 
        :index="index => index + 1" width="80" :label="$t('common.no')"/>
      <el-table-column v-if="explicitImplicitColumn.jobName.visible" align="center" show-overflow-tooltip prop="jobName" 
        :label="$t('job.job name')"/>
      <el-table-column v-if="explicitImplicitColumn.jobGroupKey.visible" align="center" show-overflow-tooltip prop="jobGroupKey" 
        :label="$t('job.job group key')"/>
      <el-table-column v-if="explicitImplicitColumn.jobKey.visible" align="center" show-overflow-tooltip prop="jobKey" 
        :label="$t('job.job key')"/>
      <el-table-column v-if="explicitImplicitColumn.tkGroupKey.visible" align="center" show-overflow-tooltip prop="tkGroupKey" 
        :label="$t('job.trigger group key')"/>
      <el-table-column v-if="explicitImplicitColumn.tkKey.visible" align="center" show-overflow-tooltip prop="tkKey" 
        :label="$t('job.trigger key')"/>
      <el-table-column v-if="explicitImplicitColumn.cron.visible" align="center" show-overflow-tooltip prop="cron" 
        :label="$t('job.cron expression')"/>
      <el-table-column v-if="explicitImplicitColumn.started.visible" align="center" show-overflow-tooltip prop="started" 
        :label="$t('job.start status')">
        <template #default="scope">
          <el-switch :model-value="scope.row.started" inline-prompt :active-text="$t('job.start')" :inactive-text="$t('job.stop')"
            :loading="switchLoading" @click="switchLoading = true;editJob = scope.row;editJob.started = !editJob.started;requestEditJob();" 
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"/>
        </template>
      </el-table-column>
      <el-table-column v-if="explicitImplicitColumn.operator.visible" align="center" :label="$t('common.operator')">
        <template #default="scope">
          <el-link style="margin: 0px 3px 0px 3px;" :underline="false" icon="Edit" @click="openEditJob(scope.row)">
            {{$t('common.edit')}}
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-model:current-page="jobSearch.pageNum" v-model:page-size="jobSearch.pageSize" :page-sizes="[10, 20, 50, 100]" :total="jobSearch.total"
      @current-change="jobList" @size-change="jobList"/>

    <!-- 任务/编辑 -->
    <el-dialog v-model="dialogOpen.visibleEdit" :title="$t('common.edit')" width="40%"
        draggable :close-on-click-modal="false" @close="editJob = {}">
      <el-form :model="editJob" ref="editJobRef" label-width="auto" inline-message :rules="{
          jobName: [{ required: true, message: $t('job.please input job name'), trigger: 'blur' }],
          cron: [{ required: true, message: $t('job.please input cron expression'), trigger: 'blur' }],
          }">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('job.job name')+':'" prop="jobName">
              <el-input v-model="editJob.jobName" maxlength="255"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('job.cron expression')+':'" prop="cron">
              <el-popover placement="bottom" trigger="click" width="auto">
                <template #reference>
                  <el-input v-model="editJob.cron" maxlength="255" :disabled="true"/>
                </template>
                <CronExpression v-model="editJob.cron"/>
              </el-popover>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('job.start status')+':'" prop="cron">
              <el-switch v-model="editJob.started" inline-prompt :active-text="$t('job.start')" :inactive-text="$t('job.stop')" 
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"/>
            </el-form-item>
          </el-col>
        </el-row>
        <div style="display: flex;justify-content: center;">
          <el-button type="info" @click="dialogOpen.visibleEdit = false">{{$t('common.cancel')}}</el-button>
          <el-button type="primary" @click="handleEditJob">{{$t('common.confirm')}}</el-button>
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
import CronExpression from '@/components/CronExpression';
import { apiJobList, apiEditJob } from '@/api/job';

const i18n = useI18n();

const loading = ref(false);
const switchLoading = ref(false);
const visibleSearch = ref(true);
const dialogOpen = reactive({
  visibleEdit: false,
  visibleExplicitImplicitColumn: false,
});
const explicitImplicitColumn = reactive({
  no: { label: i18n.t('common.no'), visible: true },
  jobName: { label: i18n.t('job.job name'), visible: true },
  jobGroupKey: { label: i18n.t('job.job group key'), visible: false },
  jobKey: { label: i18n.t('job.job key'), visible: true },
  tkGroupKey: { label: i18n.t('job.trigger group key'), visible: false },
  tkKey: { label: i18n.t('job.trigger key'), visible: true },
  cron: { label: i18n.t('job.cron expression'), visible: true },
  started: { label: i18n.t('job.start status'), visible: true },
  operator: { label: i18n.t('common.operator'), visible: true },
});
const jobSearch = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  dateRange: [],
});

let jobListData = ref([]);
let editJob = ref({});

const openEditJob = (job) => {
  dialogOpen.visibleEdit = true;
  editJob.value = JSON.parse(JSON.stringify(job));
}

const editJobRef = ref();
const handleEditJob = () => {
  editJobRef.value.validate((valid) => {
    if (!valid) {
      return;
    }
    requestEditJob();
  })
}

const requestEditJob = () => {
  apiEditJob(editJob.value).then(([data, headers]) => {
    ElMessage({ type: 'success', message: i18n.t('common.success') });
    dialogOpen.visibleEdit = false;
    jobList();
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  }).finally(() => {
    switchLoading.value = false;
  });
}

const jobList = () => {
  apiJobList(jobSearch).then(([data, headers]) => {
    jobListData.value = data.list;
    jobSearch.pageNum = data.pageNum;
    jobSearch.pageSize = data.pageSize;
    jobSearch.total = data.total;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  })
}

onMounted(() => {
  jobList();
});
</script>

<style lang="less" scoped>
.job-body {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

:deep(.el-input.is-disabled) {
  cursor: pointer;
  & .el-input__inner {
    cursor: pointer;
  }
}
</style>