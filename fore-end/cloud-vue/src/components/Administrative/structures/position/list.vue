<template>
    <el-row>
        <el-row type="flex" justify="end" align="middle" class="el-table-panel">
            <router-link class="el-button el-button--primary el-button--small" to="add">
                <i class="el-icon-plus"></i>&nbsp;添加岗位
            </router-link>
        </el-row>
        <el-table :data="tableData" style="width: 100%" @selection-change="selectItem" :highlight-current-row="true"
                  border :fit="true">
            <el-table-column type="selection"></el-table-column>
            <el-table-column label="岗位名称" align="center" prop="name"></el-table-column>
            <el-table-column label="状态" align="center" prop="status">
                <template slot-scope="scope">
                    {{ scope.row.status | status }}
                </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark"></el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
  					<span>
  						<router-link :to="{ name: 'positionEdit', params: { id: scope.row.id }}"
                                     class="el-button el-button--primary el-button--small">
  						    编辑
  						</router-link>
  					</span>
                    <el-button size="small" type="danger" @click="confirmDelete(scope.row)">删除</el-button>
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
        this.$confirm('确认删除该岗位?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$global.openGlobalLoading();
          this.apiDelete('admin/posts/delete/', item.id).then((res) => {
            this.$global.closeGlobalLoading();
            this.handelResponse(res, () => {
              this.$message.success('删除成功');
              this.$router.history.go(-1);
            })
          })
        }).catch(() => {
        })
      },
      getPositions() {
        this.apiGet('admin/posts').then((res) => {
          this.handelResponse(res, (data) => {
            this.tableData = data
          })
        })
      }
    },
    created() {
      this.getPositions()
    },
    computed: {},
    components: {},
    mixins: [http]
  }
</script>
