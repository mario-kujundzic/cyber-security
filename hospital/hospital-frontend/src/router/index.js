import Vue from "vue"
import VueRouter from "vue-router"

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
};

Vue.use(VueRouter);

import Login from '../views/Login';

const routes = [
    {
        component: Login,
        name: 'Login',
        path: '/login'
    }
]

export default new VueRouter({
    mode: 'history',
    routes: routes
});