import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import axios from 'axios'
import storage from 'lockr'
import Cookies from 'js-cookie'
import _ from 'lodash'
import moment from 'moment'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import routes from './routes'
import VueRouter from 'vue-router'
import store from './vuex/store'
import filter from './assets/js/filter'
import global from './assets/js/global'
import 'nprogress/nprogress.css'
import 'assets/css/global.css'
import 'assets/css/base.css'

axios.defaults.baseURL = HOST;
axios.defaults.timeout = 1000 * 15;
axios.defaults.withCredentials = true;
axios.defaults.headers.authKey = storage.get('authKey');
axios.defaults.headers.sessionId = storage.get('sessionId');
axios.defaults.headers['Content-Type'] = 'application/json';

axios.interceptors.request.use(
  // 这里的config包含每次请求的内容
  config => {
    if (store.state.authKey) {
      config.headers.authKey = storage.get('authKey');
      config.headers.sessionId = storage.get('sessionId')
    }
    return config
  }, error => {
    return Promise.reject(error)
  });

const router = new VueRouter({
  mode: 'history',
  base: __dirname,
  routes
});

router.beforeEach((to, from, next) => {
  const hideLeft = to.meta.hideLeft;
  store.dispatch('showLeftMenu', hideLeft);
  store.dispatch('showLoading', true);
  let authKey = storage.get('authKey');
  if (to.meta.requireAuth) {  // 判断该路由是否需要登录权限
    if (authKey) {
      next();
    } else {
      next({
        path: '/login',
        query: { redirect: to.fullPath }  // 将跳转的路由path作为参数，登录成功后跳转到该路由
      })
    }
  } else {
    next();
  }
});
axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          storage.rm('authKey');
          storage.rm('sessionId');
          router.replace({
            path: '/login',
            query: { redirect: router.currentRoute.fullPath }
          });
      }
    }
    return Promise.reject(error.response.data)
  });

Vue.use(VueRouter);
Vue.use(ElementUI, { size: 'small', zIndex: 3000 });
window.store = store;
window.HOST = HOST;
window._ = _;
window.moment = moment;
window.pageSize = 15;

Vue.prototype.$cookies = Cookies;
Vue.prototype.$global = global;
Vue.prototype.$storage = storage;
const bus = new Vue();
window.bus = bus;

new Vue({
  el: '#app',
  template: '<App/>',
  filters: filter,
  router,
  store,
  components: { App }
}).$mount('#app');
