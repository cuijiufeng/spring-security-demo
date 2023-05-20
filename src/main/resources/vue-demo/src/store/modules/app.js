import {defineStore} from 'pinia';
import {LOGIN_INFO} from '@/utils/config'
import useUserStore from '@/store/modules/user'

const useAppStore = defineStore(
  'app',
  {
    state: () => ({}),
    actions: {
      appCreate() {
        //如果已经登录，是刷新操作的时候恢复用户信息
        if(sessionStorage.getItem(LOGIN_INFO)) {
          useUserStore().login(JSON.parse(sessionStorage.getItem(LOGIN_INFO)));
        }
      }
    }
  }
)

export default useAppStore