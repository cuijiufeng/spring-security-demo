<template>
  <div class="login-body">
    <canvas id="login-canvas"/>
    <language style="position: absolute;top: 10%;right: 7%;"/>
    <el-form class="login-form" :model="loginForm" ref="loginForm" :rules="{
        username: [{ required: true, message: this.$t('login.please input username'), trigger: 'blur' }],
        password: [{ required: true, message: this.$t('login.please input password'), trigger: 'blur' }],
        verifyCode: [{ required: true, message: this.$t('login.please input verify code'), trigger: 'blur' }],
      }">
        <img class="logo-img" style="width: 220px;height: 60px;margin-bottom: 10px;" src="@/assets/logo/logo.png">
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" :placeholder="$t('login.please input username')"
          prefix-icon="User"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" :type="passwordHide ? 'password' : 'text'" 
          :placeholder="$t('login.please input password')" prefix-icon="Key">
          <template #suffix>
            <el-icon style="cursor: pointer" @click="passwordHide = !passwordHide">
              <View v-if="passwordHide"/>
              <Hide v-else/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="verifyCode">
        <img style="height: 32px;width: 100px;margin-right: 40px;" src="@/assets/logo/logo.png"/>
        <el-input style="width: calc(100% - 140px);" v-model="loginForm.verifyCode" :placeholder="$t('login.please input verify code')"/>
      </el-form-item>
      <el-button style="margin: 0 auto;width: 150px;" type="primary" @click="onSubmit">{{ $t('login.login') }}</el-button>
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
      passwordHide: true,
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
        login(this.loginForm).then(([data, headers]) => {
          this.$store.commit('login', data);
          this.$router.replace("/");
        }).catch(([err, headers]) => {
          this.$message({ type: 'error', message: err.message, });
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
  .login-form {
    width: 30vw;
    padding: 4vh 5vw 5vh 5vw;
    text-align: center;
    background: rgba(255, 255, 255, 0.1);;
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