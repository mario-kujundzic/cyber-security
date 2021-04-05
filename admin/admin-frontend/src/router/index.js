import Vue from "vue"
import VueRouter from "vue-router"

Vue.use(VueRouter);

import Login from '../views/Login';
import Home from '../views/Home';

const routes = [
    {
        component: Home,
        name: 'Home',
        path: '/',
        beforeEnter : guardRouteLoggedIn,
        
    },
    {
        component: Login,
        name: 'Login',
        path: '/login',
    },
]

//todo: token expired
function guardRouteLoggedIn(to, from, next) {
    let user = JSON.parse(localStorage.getItem('user'));
    if(!user || !user['token'])
        next('/login');
    else
        next(); // allow to enter the route
}

/*function guardRouteAdmin(to, from, next) {
    let user = localStorage.getItem('user');
    if(user['role'] === 'ADMIN')
        next();
}*/

/*function guardRouteDoctor(to, from, next) {
    let user = localStorage.getItem('user');
    if(user['role'] === 'DOCTOR')
        next();
}*/

export default new VueRouter({
    mode: 'history',
    routes: routes
});