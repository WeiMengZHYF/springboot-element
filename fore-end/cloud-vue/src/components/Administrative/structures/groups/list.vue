<template>
    <el-row>
        <el-row type="flex" justify="end" align="middle" class="el-table-panel">
            <router-link class="el-button el-button--primary el-button--small" to="add">
                <i class="el-icon-plus"></i>添加用户组
            </router-link>
        </el-row>
        <el-table :data="tableData" @selection-change="selectItem" :highlight-current-row="true" border :fit="true">
            <el-table-column type="selection"></el-table-column>
            <el-table-column align="center" label="组名" prop="title"></el-table-column>
            <el-table-column align="center" label="描述" prop="remark"></el-table-column>
            <el-table-column align="center" label="状态" prop="status">
                <template scope="scope">
                    {{ scope.row.status | status }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template scope="scope">
  					<span>
  						<router-link :to="{ name: 'groupsEdit', params: { id: scope.row.id }}" class="el-button el-button--primary el-button--small">
  						    编辑
  						</router-link>
  					</span>
                    <el-button size="small" type="danger" @click="confirmDelete(scope.row)"> 删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-row>
</template>

<script>
  import btnGroup from '../../../Common/btn-group.vue'
  import http from '../../../../assets/js/http'

  export default {
    data() {
      return {
        tableData: [],
        multipleSelection: []
      }
    },
    methods: {
      selectItem(val) {
        this.multipleSelection = val
      },
      confirmDelete(item) {
        this.$confirm('确认删除该用户组?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$global.openGlobalLoading();
          this.$http.apiDelete('admin/groups/delete/', item.id).then((res) => {
            this.$global.closeGlobalLoading();
            this.$http.handelResponse(res, () => {
              this.$message.success('删除成功');
              this.$router.history.go(-1);
            })
          })
        }).catch(() => this.$message.error('删除失败'))
      },
      getgroups() {
        this.$http.apiGet('admin/groups').then((res) => {
          this.$http.handelResponse(res, (data) => {
            this.tableData = data
          })
        })
      }
    },
    created() {
      this.getgroups()
    },
    computed: {},
    components: {
      btnGroup
    },
    mixins: [http]
  }
</script>
