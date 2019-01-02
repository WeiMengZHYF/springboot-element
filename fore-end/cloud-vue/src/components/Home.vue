<template>
    <el-container>
        <el-header>
            <el-col :span="4">
                <template v-if="logo_type === '1'">
                    <img :src="img" class="logo">
                </template>
                <template v-else>
                    <span class="p-l-20">{{title}}</span>
                </template>
            </el-col>
            <el-col :span="16" class="ofv-hd">
                <div class="fl p-l-20 p-r-20 top-menu" :class="{'top-active': menu.selected}" v-for="menu in topMenu"
                     @click="switchTopMenu(menu)">
                    {{menu.title}}
                </div>
            </el-col>
            <el-col :span="4">
                <el-dropdown @command="handleMenu" class="user-menu">
                    <span style="cursor: default;color: #ffff">
                        {{username}}&nbsp;&nbsp<i class="fa fa-user" aria-hidden="true"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="changePwd">修改密码</el-dropdown-item>
                        <el-dropdown-item command="logout">退出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-col>
        </el-header>
        <el-container>
            <el-aside v-bind:style="asideStyle">
                <leftMenu :menuData="menuData" :menu="menu" ref="leftMenu"/>
            </el-aside>
            <el-main>
                <transition name="fade" mode="out-in" appear>
                    <router-view v-loading="showLoading"></router-view>
                </transition>
            </el-main>
        </el-container>
        <changePwd ref="changePwd"></changePwd>
    </el-container>
</template>
<style>
    .logo {
        width: 150px;
        float: left;
        margin: 10px 10px 10px 18px;
    }
</style>
<script>
  import leftMenu from './Common/leftMenu.vue'
  import changePwd from './Account/changePwd.vue'
  import http from '../assets/js/http'

  export default {
    data() {
      return {
        username: '',
        topMenu: [],
        childMenu: [],
        menuData: [],
        hasChildMenu: false,
        menu: null,
        module: null,
        img: '',
        title: '',
        logo_type: null,
        asideStyle: {
          width: '220px',
          minHeight: 1000 + "px"
        }
      }
    },
    methods: {
      logout() {
        this.$confirm('确认退出吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(() => {
          this.$global.openGlobalLoading();
          let data = {
            sessionId: this.$storage.get('sessionId')
          };
          this.$http.apiPost('admin/logout', data).then((res) => {
            this.$global.closeGlobalLoading();
            this.$http.handelResponse(res, () => {
              this.$storage.rm('menus');
              this.$storage.rm('authKey');
              this.$storage.rm('rememberKey');
              this.$storage.rm('authList');
              this.$storage.rm('userInfo');
              this.$storage.rm('sessionId');
              this.$cookies.remove('rememberPwd');
              this.$message.success('退出成功');
              this.$router.replace('/');
            })
          })
        }).catch(() => {
          this.$message.error('退出登录失败');
        })
      },
      switchTopMenu(item) {
        if (!item.child) {
          this.$router.push(item.url)
        } else {
          this.$router.push(item.child[0].child[0].url)
        }
      },
      handleMenu(val) {
        switch (val) {
          case 'logout':
            this.logout();
            break;
          case 'changePwd':
            this.changePwd();
            break
        }
      },
      changePwd() {
        this.$refs.changePwd.open()
      },
      getTitleAndLogo() {
        this.$http.apiPost('admin/configs').then((res) => {
          this.$http.handelResponse(res, (data) => {
            document.title = data.SYSTEM_NAME;
            this.logo_type = data.LOGO_TYPE;
            this.title = data.SYSTEM_NAME;
            this.img = window.HOST + data.SYSTEM_LOGO
          })
        })
      },
      getUsername() {
        this.username = this.$storage.get('userInfo').username
      }
    },
    created() {

      this.asideStyle.minHeight = document.body.offsetHeight - 60 + "px";
      const that = this;
      window.onresize = () => {
        return (() => {
          that.asideStyle.minHeight = document.body.offsetHeight - 60 + "px";
        })();
      };

      this.getTitleAndLogo();
      let authKey = this.$storage.get('authKey');
      let sessionId = this.$storage.get('sessionId');
      if (!authKey || !sessionId) {
        this.$message.warning('您尚未登录');
        this.$route.replace('/');
        return
      }
      this.getUsername();
      let menus = this.$storage.get('menus');
      this.menu = this.$route.meta.menu;
      this.module = this.$route.meta.module;
      this.topMenu = menus;
      _(menus).forEach(entity => {
        if (entity.module === this.module) {
          this.menuData = entity.child;
          entity.selected = true
        } else {
          entity.selected = false
        }
      })
    },
    computed: {
      showLeftMenu() {
        this.hasChildMenu = store.state.showLeftMenu;
        return store.state.showLeftMenu
      }
    },
    components: {
      leftMenu,
      changePwd
    },
    watch: {
      '$route'(to, from) {
        _(this.topMenu).forEach(entity => {
          if (entity.module === to.meta.module) {
            entity.selected = true;
            if (!to.meta.hideLeft) {
              this.menu = to.meta.menu;
              this.menuData = entity.child
            }
          } else {
            entity.selected = false
          }
        })
      }
    }
  }
</script>
