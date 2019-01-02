<template>
    <el-row>
        <el-row>
            <el-col :span="8" style="margin-bottom: 15px">
                <el-input placeholder="请输入用户名" v-model="realname">
                    <el-button slot="append" icon="search" @click="getAllUsers">查询</el-button>
                </el-input>
            </el-col>
        </el-row>
        <el-row type="flex" justify="end" align="middle" class="el-table-panel">
            <router-link class="el-button el-button--primary el-button--small" to="add">
                <i class="el-icon-plus"></i>&nbsp;&nbsp;添加用户
            </router-link>
        </el-row>
        <el-table :data="tableData" @selection-change="selectItem" :highlight-current-row="true" border :fit="true">
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column align="center" prop="structureName" label="所属组织架构"></el-table-column>
            <el-table-column align="center" prop="username" label="用户名" ></el-table-column>
            <el-table-column align="center" prop="remark" label="备注" ></el-table-column>
            <el-table-column align="center" label="状态" >
                <template slot-scope="scope" align="center">
                    {{ scope.row.status | status }}
                </template>
            </el-table-column>
            <el-table-column label="操作" >
                <template slot-scope="scope" align="center">
                    <span>
                        <router-link :to="{ name: 'usersEdit', params: { id: scope.row.id }}" class="el-button el-button--primary el-button--small"> 编辑 </router-link>
                    </span>
                    <el-button size="small" type="danger" @click="confirmDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row class="pos-rel p-t-20">
            <el-row class="block pages">
                <el-pagination @current-change="handleCurrentChange" layout="prev, pager, next" :page-size="limit" :current-page="currentPage" :total="dataCount">
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
        dataCount: null,
        currentPage: null,
        realname: '',
        multipleSelection: [],
        limit: 1
      }
    },
    methods: {
      selectItem(val) {
        this.multipleSelection = val
      },
      handleCurrentChange(page) {
        this.$router.push({ path: this.$route.path, query: { realname: this.realname, page: page } })
      },
      confirmDelete(item) {
        this.$confirm('确认删除该用户?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$global.openGlobalLoading();
          this.$http.apiPost('admin/users/delete', item).then((res) => {
            this.$global.closeGlobalLoading();
            this.$http.handelResponse(res, () => {
              this.$message.success('删除成功');
              this.$router.history.go(-1);
            })
          })
        }).catch(() => {
          this.$message.error('删除失败');
        })
      },
      getAllUsers() {
        this.loading = true;
        const data = {
          params: {
            realname: this.realname,
            page: this.currentPage,
            rows: this.limit
          }
        };
        this.$http.apiPost('admin/users', data).then((res) => {
          this.$http.handelResponse(res, (data) => {
            this.tableData = data.list;
            this.dataCount = data.total
          })
        })
      },
      getCurrentPage() {
        let data = this.$route.query;
        if (data) {
          if (data.page) {
            this.currentPage = parseInt(data.page)
          } else {
            this.currentPage = 1
          }
        }
      },
      getRealname() {
        let data = this.$route.query;
        if (data) {
          if (data.realname) {
            this.realname = data.realname
          } else {
            this.realname = ''
          }
        }
      },
      init() {
        this.getRealname();
        this.getCurrentPage();
        this.getAllUsers()
      }
    },
    created() {
      this.init()
    },
    computed: {
    },
    watch: {
      '$route'(to, from) {
        this.init()
      }
    },
    components: {
    }
  }
</script>
