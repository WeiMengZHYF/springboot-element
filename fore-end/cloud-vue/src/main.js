import 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import axios from 'axios'
import Lockr from 'lockr'
import Cookies from 'js-cookie'
import _ from 'lodash'
import moment from 'moment'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import routes from './routes'
import VueRouter from 'vue-router'
import store from './vuex/store'
import filter from './assets/js/filter'
import _g from './assets/js/global'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import 'assets/css/global.css'
import 'assets/css/base.css'


axios.defaults.baseURL = HOST
axios.defaults.timeout = 1000 * 15
axios.defaults.withCredentials = true
axios.defaults.headers.authKey = Lockr.get('authKey')
axios.defaults.headers.sessionId = Lockr.get('sessionId')
axios.defaults.headers['Content-Type'] = 'application/json'

axios.interceptors.request.use(
    // 这里的config包含每次请求的内容
    config => {
        console.info('authKey :' +  Lockr.get('authKey'));
        if (store.state.authKey) {
            config.headers.authKey = Lockr.get('authKey')
            config.headers.sessionId = Lockr.get('sessionId')
        }
        return config
    }, error => {
        return Promise.reject(err)
    })

const router = new VueRouter({
    mode: 'history',
    base: __dirname,
    routes
})

router.beforeEach((to, from, next) => {
    const hideLeft = to.meta.hideLeft
    store.dispatch('showLeftMenu', hideLeft)
    store.dispatch('showLoading', true)
    //NProgress.start()
    //next()
    console.log('authKey :' +  Lockr.get('authKey'));
    let authKey = Lockr.get('authKey');
    if (to.meta.requireAuth) {  // 判断该路由是否需要登录权限
        if (authKey) {
            next();
        } else {
            console.log('to login- current Path :'+to.fullPath);
            next({
                path: '/login',
                query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
            })
        }
    } else {
        next();
    }
})
// http response 拦截器
axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            console.log('请求失败 : '+error);
            switch (error.response.status) {
                case 401:
                    Lockr.rm('authKey');
                    Lockr.rm('sessionId');
                    console.info(router.currentRoute.fullPath);
                    router.replace({
                        path: '/login',
                        query: {redirect: router.currentRoute.fullPath}
                    });
            }
        }
        return Promise.reject(error.response.data)   // 返回接口返回的错误信息
    });

router.afterEach(transition => {
    NProgress.done()
})

Vue.use(ElementUI)
Vue.use(VueRouter)

window.router = router
window.store = store
window.HOST = HOST
window.axios = axios
window._ = _
window.moment = moment
window.Lockr = Lockr
window.Cookies = Cookies
window._g = _g
window.pageSize = 15

const bus = new Vue()
window.bus = bus

new Vue({
    el: '#app',
    template: '<App/>',
    filters: filter,
    router,
    store,
    components: {App}
// render: h => h(Login)
}).$mount('#app')
