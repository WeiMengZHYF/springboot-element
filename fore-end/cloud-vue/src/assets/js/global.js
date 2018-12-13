import storage from 'lockr'

const commonFn = {
  j2s(obj) {
    return JSON.stringify(obj)
  },
  closeGlobalLoading() {
    setTimeout(() => {
      store.dispatch('showLoading', false)
    }, 0)
  },
  openGlobalLoading() {
    setTimeout(() => {
      store.dispatch('showLoading', true)
    }, 0)
  },
  clearVuex(cate) {
    store.dispatch(cate, [])
  },
  getHasRule(val) {
    const moduleRule = 'admin';
    let userInfo = storage.get('userInfo');
    if (userInfo.id === 1) {
      return true
    } else {
      let authList = moduleRule + storage.get('authList');
      return _.includes(authList, val)
    }
  }
};

export default commonFn
