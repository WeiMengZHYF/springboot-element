<template>
    <el-row>
        <el-form ref="form" :model="form" :rules="rules" label-width="130px">
            <el-form-item label="部门名称" prop="name">
                <el-input v-model.trim="form.name"></el-input>
            </el-form-item>
            <el-form-item label="上级部门" prop="pid">
                <el-select v-model="form.pid" placeholder="上级部门">
                    <el-option v-for="item in options" :label="item.name" :value="item.id"></el-option>
                </el-select>
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
          pid: ''
        },
        users: [],
        options: [{ pid: 0, name: '无' }],
        rules: {
          name: [
            { required: true, message: '请输入部门名称', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      add(form) {
        this.$refs[form].validate((valid) => {
          if (valid) {
            this.isLoading = !this.isLoading;
            this.$http.apiPost('admin/structures/save', this.form).then((res) => {
              this.$http.handelResponse(res, () => {
                this.$message.success('添加成功');
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
          this.$http.handelResponse(res, data => this.options = this.options.concat(data.list))
        })
      }
    },
    created() {
      this.getStructures()
    },
    mixins: [http, fomrMixin]
  }
</script>
