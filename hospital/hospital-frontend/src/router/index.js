import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

import Login from "../views/auth/Login";
import ForgotPassword from "../views/auth/ForgotPassword"
import ResetPassword from "../views/auth/ResetPassword"
import Home from "../views/home/Home";
import CSRForm from "../components/certificates/CSRForm";
import PublicKeyViewer from "../components/certificates/PublicKeyViewer";
import ManageDevices from "../views/home/ManageDevices";
import Error404 from "../views/errors/Error404";
import ManageUsers from "../views/ManageUsers";
import ManageAddUserRequests from "../views/ManageAddUserRequests";
import ManageDeleteUserRequests from "../views/ManageDeleteUserRequests";
import ManageModifyUserRequests from "../views/ManageModifyUserRequests";

const routes = [
  {
    component: Login,
    name: "Login",
    path: "/login",
  },
  {
    component: ForgotPassword,
    name: "ForgotPassword",
    path: "/forgot-password",
  },
  {
    component: ResetPassword,
    name: "ResetPassword",
    path: "/reset-password/:key",
  },
  {
    component: Home,
    path: "/",
    beforeEnter: guardRouteLoggedIn,
    children: [
      {
        component: CSRForm,
        name: "CSRForm",
        path: "/csr",
      },
      {
        component: PublicKeyViewer,
        name: "PublicKeyViewer",
        path: "/key",
      },
      {
        component: ManageDevices,
        name: "ManageDevices",
        path: "/devices",
      },
      {
        component: ManageUsers,
        name: "ManageUsers",
        path: "/users",
      },
      {
        component: ManageAddUserRequests,
        name: "ManageAddUserRequests",
        path: "add-user-requests",
      },
      {
        component: ManageDeleteUserRequests,
        name: "ManageDeleteUserRequests",
        path: "delete-user-requests",
      },
      {
        component: ManageModifyUserRequests,
        name: "ManageModifyUserRequests",
        path: "modify-user-requests",
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
  if (!user || user["token"] === undefined) next("/login");
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
