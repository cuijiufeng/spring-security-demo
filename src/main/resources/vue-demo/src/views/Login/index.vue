<template>
  <div class="login-body">
    <canvas id="login-canvas" style="width: 100%;height: 100%"/>
    <language class="lang"/>
    <el-form class="login-form" :model="loginForm" ref="loginForm" label-width="70px" :rules="{
        username: [{ required: true, message: this.$t('login.please input username'), trigger: 'blur' }],
        password: [{ required: true, message: this.$t('login.please input password'), trigger: 'blur' }],
        verifyCode: [{ required: true, message: this.$t('login.please input verify code'), trigger: 'blur' }],
      }">
        <img class="logo-img" style="width: 220px;height: 60px;margin-bottom: 10px;" src="@/assets/logo/logo.png">
      <el-form-item :label="$t('login.username')+':'" prop="username">
        <el-input v-model="loginForm.username" :placeholder="$t('login.please input username')"/>
      </el-form-item>
      <el-form-item :label="$t('login.password')+':'" prop="password">
        <el-input v-model="loginForm.password" type="password" :placeholder="$t('login.please input password')"/>
      </el-form-item>
      <el-form-item prop="verifyCode">
        <img style="height: 32px;width: 100px;margin-right: 40px;" src="@/assets/logo/logo.png"/>
        <el-input style="width: calc(100% - 140px);" v-model="loginForm.verifyCode" :placeholder="$t('login.please input verify code')"/>
      </el-form-item>
      <el-button style="margin: 0 auto" type="primary" @click="onSubmit">{{ $t('login.login') }}</el-button>
    </el-form>
  </div>
</template>

<script>
import { loginBgRender } from "@/assets/js/canvas";
import { login } from '@/api/system'
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        verifyCode: '',
      },
    }
  },
  methods: {
    onSubmit() {
      this.$refs["loginForm"].validate((valid) => {
        if (!valid) {
          return false;
        }
        login(this.loginForm).then(([data]) => {
          this.$store.commit('login', data);
          this.$router.replace("/");
        }).catch(([err]) => {
          this.$message({ type: 'error', message: err.errMsg, });
        });
      });
    },
  },
  mounted() {
    loginBgRender();
  }
}
</script>

<style lang="less" scoped>
.login-body {
  width: 100vw;
  height: 100vh;
  background: #000000;
  .lang {
    position: absolute;
    top: 10%;
    right: 7%;
  }
  .login-form {
    width: 30vw;
    height: 35vh;
    padding: 4vh 5vw 5vh 5vw;
    text-align: center;
    border: 1px solid #424242;
    background: rgba(50, 50, 50, 0.7);
    border-radius: 3px;
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