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
          <img style="position: absolute;right: 5px;bottom: 8px;width: 65px;" src="@/assets/imgs/check_mark.png"/>
        </div>
        <div style="margin-left: 50px;color: #7a8495;font-size: 12px;">
          <div style="margin-bottom: 10px;font-size: 14px;font-weight:bold">{{ $t('license.you have uploaded a valid license') }}</div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.period of validity') }}：</span>
            <span>2023-3-21 to 2023-6-21</span>
          </div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.name') }}：</span>
            <span>data-security-gateway</span>
          </div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.IP Address') }}：</span>
            <span>192.168.0.206</span>
          </div>
          <div style="margin-bottom: 10px;">
            <span style="font-weight:bold">{{ $t('license.MAC Address') }}：</span>
            <span>00:0c:29:a1:66:39</span>
          </div>
          <el-button style="margin-top: 20px;" icon="Refresh" type="primary" size="small">{{ $t('license.update authorization file') }}</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
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