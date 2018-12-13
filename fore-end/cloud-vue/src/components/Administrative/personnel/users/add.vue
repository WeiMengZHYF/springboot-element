<template>
    <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="用户名" prop="username">
            <el-input v-model.trim="form.username" :maxlength=12></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input v-model.trim="form.password"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realname">
            <el-input v-model.trim="form.realname"></el-input>
        </el-form-item>
        <el-form-item label="部门" prop="structure_id">
            <el-select v-model="form.structure_id" placeholder="请选择部门">
                <el-option v-for="item in orgsOptions" :label="item.title" :value="item.id"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="备注">
            <el-input v-model.trim="form.remark"></el-input>
        </el-form-item>
        <el-form-item label="用户组">
            <el-checkbox-group v-model="selectedGroups">
                <el-checkbox v-for="item in groupOptions" :label="item.title" class="form-checkbox"></el-checkbox>
            </el-checkbox-group>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="add('form')" :loading="isLoading">提交</el-button>
            <el-button @click="$router.history.go(-1)">返回</el-button>
        </el-form-item>
    </el-form>
</template>
<style type="text/css">
    .form-checkbox:first-child {
        margin-left: 15px;
    }
</style>
<script>
  import http from '../../../../assets/js/http'
  import fomrMixin from '../../../../assets/js/form_com'

  export default {
    data() {
      return {
        isLoading: false,
        form: {
          username: '',
          password: '',
          realname: '',
          structure_id: null,
          remark: '',
          groups: []
        },
        orgsOptions: [],
        groupOptions: [],
        selectedGroups: [],
        selectedIds: [],
        rules: {
          username: [
            { required: true, message: '请输入用户名' }
          ],
          password: [
            { required: true, message: '请输入用户密码' }
          ],
          realname: [
            { required: true, message: '请输入真实姓名' }
          ],
          structure_id: [
            { required: true, message: '请选择用户部门' }
          ]
        }
      }
    },
    methods: {
      selectCheckbox() {
        let temp = false;
        _(this.groupOptions).forEach((res) => {
          if (this.selectedGroups.toString().indexOf(res.title) > -1) {
            this.selectedIds.push(res.id)
          }
        });
        if (this.selectedIds.length) {
          this.form.groups = _.cloneDeep(this.selectedIds);
          temp = true
        }
        this.selectedIds = [];
        return temp
      },
      add() {
        if (!this.selectCheckbox()) {
          this.$global.toastMsg('warning', '请选择用户组');
          return
        }
        this.$refs.form.validate((pass) => {
          if (pass) {
            this.isLoading = !this.isLoading;
            this.apiPost('admin/users/save', this.form).then((res) => {
              this.handelResponse(res, () => {
                this.$global.toastMsg('success', '添加成功');
                this.$global.clearVuex('setUsers');
                setTimeout(() => {
                  this.$router.history.go(-1);
                }, 1500)
              }, () => this.isLoading = !this.isLoading)
            })
          }
        })
      },
      getAllGroups() {
        this.apiGet('admin/groups').then((res) => {
          this.handelResponse(res, (data) => {
            this.groupOptions = data
          })
        })
      },
      getAllOrgs() {
        this.apiGet('admin/structures').then((res) => {
          this.handelResponse(res, (data) => {
            this.orgsOptions = data
          })
        })
      }
    },
    created() {
      this.getAllGroups();
      this.getAllOrgs()
    },
    mixins: [http, fomrMixin]
  }
</script>
