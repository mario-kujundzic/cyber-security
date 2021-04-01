import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import VueAxios  from 'vue-axios'
import Axios from 'axios'
import router from './router/index'

Vue.config.productionTip = false

const role = Vue.observable({ role: localStorage.getItem('role') })

Object.defineProperty(Vue.prototype, '$role', {
  get () {
    return role.role;
  },
  set (value) {
    role.role = value;
  }
})

// axios config
Vue.use(VueAxios, Axios);

Vue.axios.defaults.headers['Authorization'] = localStorage.getItem('authKey');
Vue.axios.defaults.headers['Access-Control-Allow-Origin'] = '*';
Vue.axios.defaults.baseURL = 'https://localhost:9002/';

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')
