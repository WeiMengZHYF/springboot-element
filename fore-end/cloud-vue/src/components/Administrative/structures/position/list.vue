<template>
    <el-row>
        <el-row type="flex" justify="end" align="middle" class="el-table-panel">
            <router-link class="el-button el-button--primary el-button--small" to="add">
                <i class="el-icon-plus"></i>&nbsp;添加岗位
            </router-link>
        </el-row>
        <el-table :data="tableData" style="width: 100%" @selection-change="selectItem" :highlight-current-row="true"
                  border :fit="true">
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
        <el-row class="pos-rel p-t-20">
            <el-row class="block pages">
                <el-pagination @current-change="handleCurrentChange" layout="total, sizes, prev, pager, next, jumper"
                               :page-sizes="[10, 20, 30]" :page-size="10" @size-change="handleSizeChange"
                               :current-page="currentPage" :total="total">
                </el-pagination>
            </el-row>
        </el-row>
    </el-row>
</template>

<script>
  import http from '../../../../assets/js/http'

  export default {
    data() {
      return {
        tableData: [],
        multipleSelection: [],
        total: 0,
        currentPage: 1,
        pageSize: 10
      }
    },
    methods: {
      handleCurrentChange(page) {
        this.currentPage = page;
        this.getPositions();
      },
      handleSizeChange(pageSize) {
        this.pageSize = pageSize;
        this.getPositions();
      },
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
          this.$http.apiDelete('admin/posts/delete/', item.id).then((res) => {
            this.$global.closeGlobalLoading();
            this.$http.handelResponse(res, () => {
              this.$message.success('删除成功');
              this.$router.history.go(-1);
            })
          })
        }).catch(() => {
        })
      },
      getPositions() {
        this.$http.apiPost('admin/posts', { rows: this.pageSize, page: this.currentPage }).then((res) => {
          this.$http.handelResponse(res, (data) => {
            this.tableData = data.list;
            this.total = data.total;
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
