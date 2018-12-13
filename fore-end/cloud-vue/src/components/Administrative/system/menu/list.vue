<template>
    <el-row>
        <el-row type="flex" justify="end" align="middle" class="el-table-panel">
            <router-link class="el-button el-button--primary el-button--small" to="add">
                <i class="el-icon-plus"></i>&nbsp;&nbsp;添加菜单
            </router-link>
        </el-row>
        <el-table :data="tableData" @selection-change="selectItem" :highlight-current-row="true" border :fit="true">
            <el-table-column type="selection" :context="_self"></el-table-column>
            <el-table-column align="center" prop="p_title" label="上级菜单"></el-table-column>
            <el-table-column align="center" prop="title" label="标题"></el-table-column>
            <el-table-column align="center" prop="sort" label="排序"></el-table-column>
            <el-table-column align="center" label="状态">
                <template slot-scope="scope" align="center">
                     {{ scope.row.status | status}}
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template slot-scope="scope" align="center">
                    <span>
                        <router-link :to="{ name: 'menuEdit', params: { id: scope.row.id }}" class="el-button el-button--primary el-button--small">
                            编辑
                        </router-link>
                    </span>
                    <el-button type="danger" @click="confirmDelete(row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-row>
</template>

<script>
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
        this.$confirm('确认删除该菜单?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$global.openGlobalLoading();
          this.apiDelete('admin/menus/delete/', item.id).then((res) => {
            this.$global.closeGlobalLoading();
            this.handelResponse(res, () => {
              this.$message.success( '删除成功');
              this.$router.history.go(-1);
            })
          })
        }).catch(() => {
          this.$message.error('删除失败');
        })
      }
    },
    created() {
      this.apiPost('admin/menus').then((res) => {
        this.handelResponse(res, (data) => {
          this.tableData = data
        })
      })
    },
    computed: {},
    components: {},
    mixins: [http]
  }
</script>
