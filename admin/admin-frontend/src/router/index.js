import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

import Login from "../views/auth/Login";
import Home from "../views/home/Home";
import AddCertificate from "../views/home/AddCertificate";
import ViewCertificates from "../views/home/ViewCertificates";
import Error404 from "../views/errors/Error404"
import ManageCertificateRequests from "../views/home/ManageCertificateRequests";

const routes = [
  {
    component: Login,
    name: "Login",
    path: "/login",
  },
  {
    component: Home,
    name: "Home",
    path: "/",
    beforeEnter: guardRouteLoggedIn,
    children: [
      {
        component: ViewCertificates,
        name: "ViewCertificates",
        path: "",
      },
      {
        component: ManageCertificateRequests,
        name: "ManageCertificateRequests",
        path: "requests",
      },
      {
        component: AddCertificate,
        name: "AddCertificate",
        path: "add-certificate/:id",
      },
    ],
  },
  {
    path: "/404",
    alias: "*",
    component: Error404
  },
];

//todo: token expired
function guardRouteLoggedIn(to, from, next) {
  let user = JSON.parse(localStorage.getItem("user"));
  if (!user || !user["token"]) next("/login");
  else next(); // allow to enter the route
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
  mode: "history",
  routes: routes,
});
