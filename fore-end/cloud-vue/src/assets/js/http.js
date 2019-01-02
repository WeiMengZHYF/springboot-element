import axios from 'axios'
import storage from 'lockr'
import Vue from 'vue'

const msgInstance = new Vue();

const apiMethods = {
  apiGet(url, data = {}) {
    return new Promise((resolve, reject) => {
      axios.get(url, data).then(response => resolve(response.data))
        .catch(error => {
          reject(error);
          msgInstance.$message.warning('请求超时，请检查网络');
        })
    })
  },
  apiPost(url, data = {}) {
    return new Promise((resolve, reject) => {
      axios.post(url, data).then(response => resolve(response.data))
        .catch(response => {
          reject(response);
          msgInstance.$message.warning('请求超时，请检查网络')
        })
    })
  },
  apiDelete(url, id) {
    return new Promise((resolve, reject) => {
      axios.delete(url + id).then(response => resolve(response.data))
        .catch(response => {
          reject(response);
          msgInstance.$message.warning('请求超时，请检查网络');
        })
    })
  },
  handelResponse(response, callback, errorCallback) {
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
            msgInstance.$message.error(res.error);
            this.$router.replace('/')
          }
          break;
        case 103:
          msgInstance.$message.error(res.error);
          this.$router.replace('/');
          break;
        default :
          msgInstance.$message.error(res.error)
      }
    } else {
      console.log('default error')
    }
  },
  resetCommonData(data) {
    _(data.menusList).forEach((res, key) => {
      if (key === 0) {
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
  },
  reAjax(url, data) {
    return new Promise((resolve, reject) => {
      axios.post(url, data).then(response => resolve(response.data))
        .catch(response => {
          reject(response);
          msgInstance.$message.warning('请求超时，请检查网络');
        })
    })
  }
};

export default apiMethods
