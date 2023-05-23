<template>
  <div class="login-body">
    <canvas id="login-canvas"/>
    <language style="position: absolute;top: 10%;right: 7%;" color="white"/>
    <el-form class="login-form" :model="loginForm" ref="loginFormRef" inline-message :rules="{
        username: [{ required: true, message: $t('login.please input username'), trigger: 'blur' }],
        password: [{ required: true, message: $t('login.please input password'), trigger: 'blur' }],
        verifyCode: [{ required: true, message: $t('login.please input verify code'), trigger: 'blur' }],
      }">
        <img class="logo-img" style="width: 220px;height: 60px;margin-bottom: 10px;" src="@/assets/logo/logo.png">
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" :placeholder="$t('login.please input username')"
          prefix-icon="User"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" :type="passwordHide ? 'password' : 'text'" show-password
          :placeholder="$t('login.please input password')" prefix-icon="Key" @keydown.enter="onSubmit" />
      </el-form-item>
      <el-form-item prop="verifyCode">
        <img style="height: 32px;width: 100px;margin-right: 20px;" :src="'data:image/jpg;base64,' + verifyCodeImg"
            @click="verifyCode"/>
        <el-input style="width: calc(100% - 120px);" v-model="loginForm.verifyCode" @keydown.enter="onSubmit"/>
      </el-form-item>
      <el-button :loading="loginLoading" style="margin: 0 auto;width: 150px;" type="primary" @click="onSubmit">
        {{ loginLoading ? $t('login.login')+'. . .' : $t('login.login')}}
      </el-button>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import Fingerprint2 from 'fingerprintjs2';
import useUserStore from '@/store/modules/user';
import { useI18n } from "vue-i18n";
import { ElMessage } from 'element-plus';
import { loginBgRender } from "@/assets/js/canvas";
import { apiLogin, apiVerifyCodeImg } from '@/api/system';

const userStore = useUserStore();

const figerprint = ref('');
const loginLoading = ref(false);
const passwordHide = ref(true);
const loginForm = reactive({
  username: '',
  password: '',
  verifyCode: '',
});

const verifyCodeImg = ref('');

const loginFormRef = ref();
const onSubmit = () => {
  loginFormRef.value.validate((valid) => {
    if (!valid) {
      return;
    }
    loginLoading.value = true;
    apiLogin({...loginForm, figerprint: figerprint.value}).then(([data, headers]) => {
      userStore.login(data)
    }).catch(([data, headers]) => {
      ElMessage({ type: 'error', message: data.message });
    }).finally(() => {
      loginLoading.value = false;
    });
  });
}

const verifyCode = () => {
  apiVerifyCodeImg({figerprint: figerprint.value}).then(([data, headers]) => {
    verifyCodeImg.value = data;
  }).catch(([data, headers]) => {
    ElMessage({ type: 'error', message: data.message });
  });
}

onMounted(() => {
  loginBgRender();
  
  Fingerprint2.get((components) => {
    const values = components.map((component, index) => {
      return component.value
    });
    // 生成最终id murmur   
    figerprint.value = Fingerprint2.x64hash128(values.join(''), 31);
    verifyCode();
  });
});
</script>

<style lang="less" scoped>
.login-body {
  width: 100vw;
  height: 100vh;
  background: #000000;
  .login-form {
    width: 27vw;
    padding: 4vh 5vw 5vh 5vw;
    text-align: center;
    background: rgba(255, 255, 255, 0.1);;
    border-radius: 10px;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -60%);
    animation: flyin 0.5s ease-in-out forwards;
  }
}
@keyframes flyin {
  0% {
    top: 0%;
    opacity: 0.5;
  }
  100% {
    top: 50%;
    opacity: 1;
  }
}
</style>