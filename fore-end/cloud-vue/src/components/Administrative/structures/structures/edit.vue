<template>
    <el-row>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="部门名称" prop="name">
                <el-input v-model.trim="form.name"></el-input>
            </el-form-item>
            <el-form-item label="父级部门" prop="pid">
                <el-select v-model="form.pid" placeholder="父级部门">
                    <el-option v-for="item in options" :label="item.name" :value="item.id"></el-option>
                </el-select>
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
          name: '',
          pid: null
        },
        options: [{ id: '0', name: '无' }],
        rules: {
          name: [
            { required: true, message: '请输入部门名称', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      edit(form) {
        this.$refs[form].validate((valid) => {
          if (valid) {
            this.isLoading = !this.isLoading;
            this.$http.apiPost('admin/structures/update', this.form).then((res) => {
              this.$http.handelResponse(res, () => {
                this.$message.success('编辑成功');
                this.$router.history.go(-1);
              }, () => {
                this.isLoading = !this.isLoading
              })
            })
          }
        })
      },
      getStructures() {
        this.$http.apiPost('admin/structures').then((res) => {
          this.$http.handelResponse(res, (data) => {
            _(data.list).forEach((ret) => {
              ret.id = ret.id.toString()
            });
            this.options = this.options.concat(data.list)
          })
        })
      },
      getStructureInfo() {
        this.form.id = this.$route.params.id;
        this.$http.apiGet('admin/structures/edit/' + this.form.id).then((res) => {
          this.$http.handelResponse(res, (data) => {
            data.pid = data.pid.toString();
            this.form.id = data.id;
            this.form.name = data.name;
            this.form.pid = data.pid
          })
        })
      }
    },
    created() {
      this.getStructures();
      this.getStructureInfo()
    },
    mixins: [http, fomrMixin]
  }
</script>
