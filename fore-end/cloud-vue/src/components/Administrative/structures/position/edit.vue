<template>
    <el-row>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="岗位名称" prop="name">
                <el-input v-model.trim="form.name"></el-input>
            </el-form-item>
            <el-form-item label="备注">
                <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入内容"
                          v-model="form.remark"></el-input>
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
          remark: ''
        },
        rules: {
          name: [
            { required: true, message: '请输入岗位名称', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      edit(form) {
        this.$refs[form].validate((valid) => {
          if (valid) {
            this.isLoading = !this.isLoading;
            this.apiPost('admin/posts/update', this.form).then((res) => {
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
      getPosInfo() {
        this.form.id = this.$route.params.id;
        this.apiGet('admin/posts/edit/' + this.form.id).then((res) => {
          this.handelResponse(res, (data) => {
            this.form.name = data.name;
            this.form.remark = data.remark
          })
        })
      }
    },
    created() {
      this.getPosInfo()
    },
    mixins: [http, fomrMixin]
  }
</script>
