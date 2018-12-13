<template>
    <el-row>
        <el-row type="flex" justify="end" align="middle" class="el-table-panel">
            <router-link class="el-button el-button--primary el-button--small" to="add">
                <i class="el-icon-plus"></i>&nbsp;添加部门
            </router-link>
        </el-row>
        <el-table :data="tableData"  style="width: 100%" @selection-change="selectItem" :highlight-current-row="true" border :fit="true">
            <el-table-column  type="selection" > </el-table-column>
            <el-table-column  label="部门结构" prop="title" align="center"></el-table-column>
            <el-table-column  label="部门名称" prop="name" align="center"></el-table-column>
            <el-table-column  label="状态"  prop="status" align="center">
                <template slot-scope="scope" align="center">
                    {{ scope.row.status | status }}
                </template>
            </el-table-column>
            <el-table-column  label="操作" width="200" align="center">
                <template slot-scope="scope" align="center">
  					<span>
  						<router-link :to="{ name: 'structuresEdit', params: { id: scope.row.id }}" class="el-button el-button--primary el-button--small">
  						    编辑
  						</router-link>
  					</span>
                    <el-button size="small"   type="danger"  @click="confirmDelete(scope.row)">删除</el-button>
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
        this.$confirm('确认删除该部门?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$global.openGlobalLoading();
          this.apiDelete('admin/structures/delete/', item.id).then((res) => {
            this.$global.closeGlobalLoading();
            this.handelResponse(res, () => {
              this.$global.toastMsg('success', '删除成功');
              setTimeout(() => {
                this.$global.shallowRefresh(this.$route.name)
              }, 1500)
            })
          })
        }).catch(() => {
          // handle error
        })
      },
      getStructures() {
        this.apiGet('admin/structures').then((res) => {
          this.handelResponse(res, (data) => {
            this.tableData = data
          })
        })
      }
    },
    created() {
      this.getStructures()
    },
    computed: {

    },
    components: {
    },
    mixins: [http]
  }
</script>
