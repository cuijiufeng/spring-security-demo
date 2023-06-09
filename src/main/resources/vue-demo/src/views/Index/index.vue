<template>
  <div class="home-body">
    <el-card style="margin-bottom: 20px;">
      <template #header>
        <span>{{ $t('system.system resource') }}</span>
      </template>
      <div style="display: flex;justify-content: space-around;">
        <div style="display: flex;color: #7a8495;" v-for="i in 5" :key="i">
          <img style="width: 140px;" src="@/assets/imgs/db.png" />
          <div>
            <div style="margin: 20px 0px 5px 0px;">数据库</div>
            <div>
              <span style="font-size: 2em;margin-right: 10px;">1</span>
              <span style="font-size: 0.8em;">个</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>
    <el-card style="flex: 1;">
      <template #header>
        <span>{{ $t('system.system resource') }}</span>
      </template>
      <div style="display: flex;justify-content: space-around;">
        <div style="display: inline-flex;flex-direction: column;align-items: center;">
          <img style="width: 35px;" src="@/assets/imgs/cpu.png" />
          <div style="color: #2C9EF7;margin: 8px 0px;">
            <span style="font-size: 1.5em;">{{ processorCount }}</span>
            <span>{{ $t('system.cores') }}</span>
          </div>
          <el-progress type="dashboard" :percentage="processorOccupancy" color="#2C9EF7" >
            <template #default="{ percentage }">
              <div style="color: #2C9EF7;">{{ percentage }}%</div>
              <div style="color: #7a8495;">{{ $t('system.usage rate') }}</div>
            </template>
          </el-progress>
        </div>
        <div style="display: inline-flex;flex-direction: column;align-items: center;">
          <img style="width: 30px;" src="@/assets/imgs/memory.png" />
          <div style="color: #FD5451;margin: 8px 0px;">
            <span style="font-size: 1.5em;">{{ memoryTotal[0] }}</span>
            <span>{{ memoryTotal[1] }}</span>
          </div>
          <el-progress type="dashboard" :percentage="memoryOccupancy" color="#FD5451" >
            <template #default="{ percentage }">
              <div style="color: #FD5451;">{{ percentage }}%</div>
              <div style="color: #7a8495;">{{ $t('system.usage rate') }}</div>
            </template>
          </el-progress>
        </div>
        <div id="diskBarChart" style="width: 25vw;height: 35vh;"/>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useI18n } from "vue-i18n";
import * as echarts from 'echarts';
import { apiSystemInfo } from '@/api/system';

const i18n = useI18n();

const processorCount = ref(0);
const processorOccupancy = ref(0);
const memoryTotal = ref([0, 'G']);
const memoryOccupancy = ref(0);
const systemInfoInterval = ref();

const fileSizeConvert = (size) => {
  const sizeUnit = ["B", "KB", "MB", "GB", "TB"];
  let decimal = 0;
  let forCnt = 0;
  while (Math.trunc(size / 1024) != 0) {
    decimal = size % 1024;
    size = Math.trunc(size / 1024);
    forCnt++;
  }
  return [parseFloat(size + '.' + decimal).toFixed(1), sizeUnit[forCnt]]
}

const fileSizeConvertGb = (size) => {
  let decimal = 0;
  for(let i = 0; i < 3; i++) {
    decimal = size % 1024;
    size = Math.trunc(size / 1024);
  }
  return parseFloat(size + '.' + decimal).toFixed(1)
}

onMounted(() => {
  var diskBarChart = echarts.init(document.getElementById('diskBarChart'));
  diskBarChart.setOption({
    title: {
      text: i18n.t('system.disk space'),
      textStyle: {
        color: '#7a8495',
        fontSize: 16,
      }
    },
    color: ['#E85C05', 'rgba(0, 0, 0, 0.05)'],
    grid: {
      top: '20%',
      bottom: '10%',
    },
    xAxis: {
      type: 'value',
      name: 'G'
    },
    yAxis: {
      type: 'category',
      // data: ['C:', 'D:', 'E:', 'F:']
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: '{b0}<br/>' + i18n.t('system.total') + ':{c0}GB<br/>' + i18n.t('system.used') + ':{c1}GB'
    },
    series: [
      {
        // data: [120, 200, 150, 80],
        type: 'bar',
        barWidth: '20px',
        z: 2
      },{
        // data: [120, 200, 150, 80],
        type: 'bar',
        barWidth: '20px',
        z: 1,
        barGap: '-100%'
      }
    ]
  });
  window.addEventListener("resize", () => { diskBarChart.resize(); });
  systemInfoInterval.value = setInterval(()=>{
    apiSystemInfo().then(([data, headers]) => {
      processorCount.value = data.processorCount;
      processorOccupancy.value = Math.trunc(data.processorOccupancy * 100);
      memoryTotal.value = fileSizeConvert(data.memoryTotal);
      memoryOccupancy.value = Math.trunc(data.memoryOccupancy * 100);
      diskBarChart.setOption({
        yAxis: {
          data: data.fileStores.map(f => f.mount)
        },
        series: [{
          data: data.fileStores.map(f => fileSizeConvertGb(f.totalSpace - f.freeSpace))
        },{
          data: data.fileStores.map(f => fileSizeConvertGb(f.totalSpace))
        }]
      });
    }).catch(([data, headers]) => {
      // ElMessage({ type: 'error', message: data.message });
    })
  }, 1000)
});

onUnmounted(() => {
  clearInterval(systemInfoInterval.value);
});
</script>

<style lang="less" scoped>
.home-body {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>