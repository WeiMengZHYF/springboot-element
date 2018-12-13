import axios from 'axios'
import global from './global'
import storage from 'lockr'

const apiMethods = {
  methods: {
    apiGet(url, data = {}) {
      return new Promise((resolve, reject) => {
        axios.get(url, data).then(response => resolve(response.data))
          .catch(error => {
            reject(error);
            global.closeGlobalLoading();
            bus.$message({
              message: '请求超时，请检查网络',
              type: 'warning'
            })
          })
      })
    },
    apiPost(url, data = {}) {
      return new Promise((resolve, reject) => {
        axios.post(url, data).then(response => resolve(response.data))
          .catch(response => {
            reject(response);
            bus.$message({
              message: '请求超时，请检查网络',
              type: 'warning'
            })
          })
      })
    },
    apiDelete(url, id) {
      return new Promise((resolve, reject) => {
        axios.delete(url + id).then(response => resolve(response.data))
          .catch(response => {
            reject(response);
            global.closeGlobalLoading();
            bus.$message({
              message: '请求超时，请检查网络',
              type: 'warning'
            })
          })
      })
    },
    handelResponse(response, callback, errorCallback) {
      global.closeGlobalLoading();
      if (response.success) {
        callback(response.data)
      } else {
        if (typeof errorCallback == 'function') {
          errorCallback()
        }
        this.handleError(response)
      }
    },
    handleError(res) {
      if (res.code) {
        switch (res.code) {
          case 101:
            if (storage.get('rememberPwd')) {
              let data = {
                rememberKey: storage.get('rememberKey')
              };
              this.reAjax('admin/relogin', data).then((res) => {
                this.handelResponse(res, (data) => {
                  this.resetCommonData(data)
                })
              })
            } else {
              global.toastMsg('error', res.error);
              setTimeout(() => {
                this.$router.replace('/')
              }, 1500)
            }
            break;
          case 103:
            global.toastMsg('error', res.error);
            setTimeout(() => {
              this.$router.replace('/')
            }, 1500);
            break;
          default :
            global.toastMsg('error', res.error)
        }
      } else {
        console.log('default error')
      }
    },
    resetCommonData(data) {
      _(data.menusList).forEach((res, key) => {
        if (key == 0) {
          res.selected = true
        } else {
          res.selected = false
        }
      });
      this.$store.state.authKey = data.authKey;
      storage.set('menus', data.menusList);           // 菜单数据
      storage.set('authKey', data.authKey);             // 权限认证
      storage.set('rememberKey', data.rememberKey);    // 记住密码的加密字符串
      storage.set('authList', data.authList);        // 权限节点列表
      storage.set('userInfo', data.userInfo);          // 用户信息
      storage.set('sessionId', data.sessionId);      // 用户sessionid
      let routerUrl = '';
      if (data.menusList[0].url) {
        routerUrl = data.menusList[0].url
      } else {
        routerUrl = data.menusList[0].child[0].child[0].url
      }
      setTimeout(() => {
        let path = this.$route.path;
        if (routerUrl !== path) {
          this.$router.replace(routerUrl)
        } else {
          global.shallowRefresh(this.$route.name)
        }
      }, 1000)
    },
    reAjax(url, data) {
      return new Promise((resolve, reject) => {
        axios.post(url, data).then((response) => {
          resolve(response.data)
        }, (response) => {
          reject(response);
          bus.$message({
            message: '请求超时，请检查网络',
            type: 'warning'
          })
        })
      })
    }
  },
  computed: {
    showLoading() {
      return store.state.globalLoading
    }
  }
};

export default apiMethods
