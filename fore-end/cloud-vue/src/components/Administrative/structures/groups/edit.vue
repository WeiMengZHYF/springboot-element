<template>
    <el-row>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="用户组名称" prop="title">
                <el-input v-model.trim="form.title"></el-input>
            </el-form-item>
            <el-form-item label="父级用户组" prop="pid">
                <el-select v-model="form.pid" placeholder="父级用户组" class="w-200">
                    <el-option v-for="item in options" :label="item.title" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="备注">
                <el-input type="textarea" :rows="2" placeholder="备注" v-model="form.remark"></el-input>
            </el-form-item>
            <el-form-item label="权限分配">
                <el-tree :data="nodes" show-checkbox default-expand-all node-key="id" ref="tree" highlight-current
                         :props="defaultProps"></el-tree>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="edit('form')" :loading="isLoading">提交</el-button>
                <el-button @click="$router.history.go(-1)">返回</el-button>
            </el-form-item>
        </el-form>
    </el-row>
</template>
<script>
  import http from '../../../../assets/js/http'
  import fomrMixin from '../../../../assets/js/form_com'

  export default {
    data() {
      return {
        isLoading: false,
        form: {
          id: null,
          title: '',
          pid: '',
          remark: '',
          rules: '',
          status: null
        },
        options: [{ pid: '0', title: '无' }],
        nodes: [],
        selectedNodes: [],
        rules: {
          title: [
            { required: true, message: '请输入用户组名称', trigger: 'blur' }
          ]
        },
        defaultProps: {
          children: 'child',
          label: 'title'
        }
      }
    },
    methods: {
      edit(form) {
        this.form.rules = this.$refs.tree.getCheckedKeys().toString();
        this.$refs[form].validate((valid) => {
          if (valid) {
            this.isLoading = !this.isLoading;
            this.apiPost('admin/groups/update', this.form).then((res) => {
              this.handelResponse(res, () => {
                this.$message.success('编辑成功');
                this.$router.history.go(-1);
              }, () => {
                this.isLoading = !this.isLoading
              })
            })
          }
        })
      },
      getRules() {
        return new Promise((resolve, reject) => {
          this.apiGet('admin/rules?type=tree').then((res) => {
            this.handelResponse(res, (data) => {
              resolve(data)
            })
          })
        })
      },
      getGroups() {
        this.apiGet('admin/groups').then((res) => {
          this.handelResponse(res, (data) => {
            _(data).forEach((ret) => {
              ret.id = ret.id.toString()
            });
            this.options = this.options.concat(data)
          })
        })
      },
      async getGroupInfo() {
        this.form.id = this.$route.params.id;
        let arr = await this.getRules();
        this.nodes = this.nodes.concat(arr);
        this.apiGet('admin/groups/edit/' + this.form.id).then((res) => {
          this.handelResponse(res, (data) => {
            this.form.title = data.title;
            this.form.pid = data.pid ? data.pid.toString() : '';
            this.form.remark = data.remark;
            this.form.status = data.status;
            let array = data.rules.split(',');
            this.$refs.tree.setCheckedKeys(array)
          })
        })
      }
    },
    created() {
      this.getGroupInfo();
      this.getGroups()
    },
    mixins: [http, fomrMixin]
  }
</script>
