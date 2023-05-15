<template>
  <div class="user-body">
    <el-form :inline="true" :model="userSearch">
      <el-form-item :label="$t('user.username')">
        <el-input v-model="userSearch.user" :placeholder="$t('user.please input username')" />
      </el-form-item>
      <el-form-item :label="$t('user.role name')">
        <el-select v-model="userSearch.region" :placeholder="$t('user.please input role name')">
          <el-option label="Zone one" value="shanghai" />
          <el-option label="Zone two" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary">{{$t('common.query')}}</el-button>
        <el-button icon="Refresh">{{$t('common.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <div>
      <el-button plain size="small" icon="Plus" type="primary">{{$t('common.add')}}</el-button>
      <el-button plain size="small" icon="EditPen" type="success">{{$t('common.edit')}}</el-button>
      <el-button plain size="small" icon="Delete" type="danger">{{$t('common.delete')}}</el-button>
      <el-button plain size="small" icon="Download" type="warning">{{$t('common.export')}}</el-button>
    </div>
    <!-- <el-table :data="userData" style="width: 100%">
      <el-table-column prop="date" label="Date" width="180" />
      <el-table-column prop="name" label="Name" width="180" />
      <el-table-column prop="address" label="Address" />
    </el-table> -->
  </div>
</template>

<script>
export default {
  name: 'User',
  data() {
    return {
      userSearch: {},
      userData: {},
    }
  },
  mounted() {

  }
}
</script>

<style lang="less" scoped>
.user-body {
  padding: 20px;
}
</style>