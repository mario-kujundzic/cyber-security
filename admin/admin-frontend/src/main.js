import Vue from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify';
import VueAxios  from 'vue-axios';
import Axios from 'axios';
import router from './router/index';
import moment from 'moment';
import https from 'https';
// import VueCookies from 'vue-cookies';

// Vue.use(VueCookies);
Vue.config.productionTip = false;

const role = Vue.observable({ role: localStorage.getItem('role') })

Object.defineProperty(Vue.prototype, '$role', {
  get () {
    return role.role;
  },
  set (value) {
    role.role = value;
  }
})

Vue.filter('formatDate', function(value) {
  if (value) {
    return moment(String(value)).format('DD-MM-YYYY ');
  }
})

// axios config
Vue.use(VueAxios, Axios);

Vue.axios.defaults.httpsAgent = new https.Agent ({ rejectUnauthorized: false});
if (localStorage.getItem('authKey') != null) {
  Vue.axios.defaults.headers['Authorization'] = localStorage.getItem('authKey');
}
// Vue.axios.defaults.headers['Access-Control-Allow-Origin'] = '*';
// Vue.axios.defaults.headers['Access-Control-Allow-Credentials'] = 'true';
// Vue.axios.defaults.withCredentials = true;
Vue.axios.defaults.baseURL = 'https://localhost:9001/';


new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')

