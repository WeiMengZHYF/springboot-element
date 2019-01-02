<template>
    <el-row>
        <el-row type="flex" justify="end" align="middle" class="el-table-panel">
            <router-link class="el-button el-button--primary el-button--small" to="add">
                <i class="el-icon-plus"></i>添加节点
            </router-link>
        </el-row>
        <el-table :data="tableData" @selection-change="selectItem" :highlight-current-row="true" border :fit="true">
            <el-table-column align="center" prop="title" label="显示名"></el-table-column>
            <el-table-column align="center" prop="parent" label="父节点"></el-table-column>
            <el-table-column align="center" prop="name" label="名称"></el-table-column>
            <el-table-column align="center" label="状态">
                <template slot-scope="scope">
                    <span>{{ scope.row.status | status}}</span>
                </template>
            </el-table-column>
            <el-table-column :align="center" label="操作">
                <template slot-scope="scope">
                    <span>
                        <router-link class="el-button el-button--primary el-button--small"
                                     :to="{ name: 'ruleEdit', params: { id: scope.row.id }}">编辑</router-link>
                    </span>
                    <el-button type="danger" @click="confirmDelete(scope.$index, scope.row)">删除</el-button>
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
      selectItem(val) {
        this.multipleSelection = val
      },
      handleCurrentChange(page) {
        this.currentPage = page;
        this.list();
      },
      handleSizeChange(pageSize) {
        this.pageSize = pageSize;
        this.list();
      },
      confirmDelete(item) {
        this.$confirm('确认删除该权限?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$global.openGlobalLoading();
          this.$http.apiDelete('admin/rules/delete/', item.id).then((res) => {
            this.$global.closeGlobalLoading();
            this.$http.handelResponse(res, () => {
              this.$message.success('删除成功');
              this.$router.history.go(-1);
            })
          })
        }).catch(() => {
          this.$message.error("操作失败");
        })
      },
      list() {
        this.$http.apiPost('admin/rules', { rows: this.pageSize, page: this.currentPage }).then((res) => {
          this.$http.handelResponse(res, (data) => {
            this.tableData = data.list;
            this.total = data.total;
          })
        })
      }
    },
    created() {
      this.list();
    },
    computed: {},
    components: {}
  }
</script>
