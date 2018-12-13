<template>
    <el-row>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="岗位名称" prop="name">
                <el-input v-model.trim="form.name" ></el-input>
            </el-form-item>
            <el-form-item label="备注">
                <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入内容"
                          v-model="form.remark"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="add('form')" :loading="isLoading">提交</el-button>
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
      add(form) {
        this.$refs[form].validate((valid) => {
          if (valid) {
            this.isLoading = !this.isLoading;
            this.apiPost('admin/posts/save/', this.form).then((res) => {
              this.handelResponse(res, () => {
                this.$global.toastMsg('success', '添加成功');
                setTimeout(() => {
                  this.$router.history.go(-1);
                }, 1500)
              }, () => {
                this.isLoading = !this.isLoading
              })
            })
          }
        })
      }
    },
    created() {
      this.$global.closeGlobalLoading()
    },
    mixins: [http, fomrMixin]
  }
</script>
