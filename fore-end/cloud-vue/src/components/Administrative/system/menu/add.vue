<template>
    <el-row>
        <el-form ref="form" :model="form" :rules="rules" label-width="110px">
            <el-form-item label="标题" prop="title">
                <el-input v-model.trim="form.title"></el-input>
            </el-form-item>
            <el-form-item label="绑定权限标识" prop="ruleName">
                <el-row>
                    <el-col :span="20">
                        <el-input v-model.trim="form.ruleName"  :disabled="true"></el-input>
                    </el-col>
                    <el-col :span="3" style="margin-left: 5px">
                        <el-button @click="openRule()">查找</el-button>
                    </el-col>
                </el-row>
            </el-form-item>
            <el-form-item label="菜单类型" prop="menuType">
                <el-radio-group v-model="form.menuType">
                    <el-radio label="1">普通三级菜单</el-radio>
                    <el-radio label="2">单页菜单</el-radio>
                    <el-radio label="3">外链</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="上级菜单" prop="pid">
                <el-select v-model="form.pid" placeholder="上级菜单">
                    <el-option v-for="item in options" :label="item.title" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="路径">
                <el-input v-model.trim="form.url"  ></el-input>
            </el-form-item>
            <el-form-item label="模块" prop="module">
                <el-input v-model.trim="form.module"  ></el-input>
            </el-form-item>
            <el-form-item label="所属菜单">
                <el-input v-model.trim="form.menu"  ></el-input>
            </el-form-item>
            <el-form-item label="排序">
                <el-input v-model="form.sort" ></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="add('form')" :loading="isLoading">提交</el-button>
                <el-button @click="$router.history.go(-1)">返回</el-button>
            </el-form-item>
        </el-form>
        <ruleList ref="ruleList"></ruleList>
    </el-row>
</template>

<script>
  import ruleList from './rule.vue'
  import http from '../../../../assets/js/http'
  import fomrMixin from '../../../../assets/js/form_com'

  export default {
    data() {
      return {
        isLoading: false,
        form: {
          title: '',
          ruleName: '',
          ruleId: null,
          pid: '',
          menuType: '',
          url: '',
          module: '',
          menu: '',
          sort: ''
        },
        options: [{ id: 0, title: '无' }],
        rules: {
          title: [
            { required: true, message: '请输入菜单标题' }
          ],
          menuType: [
            { required: true, message: '请选择菜单类型' }
          ],
          module: [
            { required: true, message: '请填写菜单模块' }
          ],
          pid: [
            { type: 'number', required: true, message: '请选择上级菜单' }
          ]
        }
      }
    },
    methods: {
      add() {
        this.$refs.form.validate((pass) => {
          if (pass) {
            this.isLoading = !this.isLoading;
            this.$http.apiPost('admin/menus/save', this.form).then((res) => {
              this.$http.handelResponse(res, () => {
                this.$global.clearVuex('setRules');
                this.$message.success('添加成功');
                setTimeout(() => {
                  this.$router.history.go(-1);
                }, 1500)
              }, () => {
                this.isLoading = !this.isLoading
              })
            })
          }
        })
      },
      openRule() {
        this.$refs.ruleList.open()
      }
    },
    created() {
      this.$http.apiPost('admin/menus').then(res => {
        this.$http.handelResponse(res, (data) => {
          let array = [];
          data.list.forEach(ret => {
            if (ret.level !== 3 && ret.menuType === 1) {
              array.push(ret)
            }
          });
          this.options = this.options.concat(array)
        })
      })
    },
    components: {
      ruleList
    },
    mixins: [http, fomrMixin]
  }
</script>
