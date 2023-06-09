<template>
  <div class="license-body">
    <div style="color: #409EFF;">
      <svg-icon icon-class="license"/>
      <span style="margin-left: 10px;">License{{ $t('license.management') }}</span>
    </div>
    <div style="flex: 1;display: flex;align-items: center;justify-content: center;">
      <div style="display: flex;align-items: center;">
        <div style="position: relative;">
          <img style="width: 200px;" src="@/assets/imgs/license.png"/>
          <img v-if="licenseData.name" style="position: absolute;right: 5px;bottom: 8px;width: 65px;" src="@/assets/imgs/check_mark.png"/>
        </div>
        <div style="margin-left: 50px;color: #7a8495;font-size: 12px;">
          <div style="margin-bottom: 10px;font-size: 14px;font-weight:bold">
            {{ licenseData.name ? $t('license.you have uploaded a valid license') : $t('license.please upload license') }}
          </div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.period of validity') }}：</span>
            <span>{{ licenseData.validityPeriod }}</span>
          </div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.name') }}：</span>
            <span>{{ licenseData.name }}</span>
          </div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.IP Address') }}：</span>
            <span>{{ licenseData.ip }}</span>
          </div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.MAC Address') }}：</span>
            <span>{{ licenseData.mac }}</span>
          </div>
          <el-upload ref="licenseUpload" action="#" :multiple="false" :limit="1" :auto-upload="false" 
            :show-file-list="false" accept=".license"
            :on-exceed="handleExceed" :on-change="handleUpload">
            <el-button style="margin-top: 20px;" icon="Refresh" type="primary" size="small">
              {{ $t('license.update authorization file') }}
            </el-button>
          </el-upload>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useI18n } from "vue-i18n";
import { ElMessage, ElMessageBox } from 'element-plus';
import { apiLicenseInfo, apiLicenseUpdate } from '@/api/system';

const i18n = useI18n();

const licenseData = ref({});

const licenseUpload = ref();
const handleExceed = (files) => {
  licenseUpload.value.clearFiles()
}

const handleUpload = (uploadFile) => {
  apiLicenseUpdate({file: uploadFile.raw}).then(([data, headers]) => {
    ElMessage({type: 'success', message: i18n.t('common.success')});
    licenseInfo();
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  })
}

const licenseInfo = () => {
  apiLicenseInfo().then(([data, headers]) => {
    if(data) {
      licenseData.value = data;
    }
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  })
}

onMounted(() => {
  licenseInfo();
});
</script>

<style lang="less" scoped>
.license-body {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>