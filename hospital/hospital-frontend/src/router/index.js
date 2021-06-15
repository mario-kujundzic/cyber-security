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
import ViewPatients from "../views/home/ViewPatients";
import ManageUsers from "../views/ManageUsers";
import ManageAddUserRequests from "../views/ManageAddUserRequests";
import ManageDeleteUserRequests from "../views/ManageDeleteUserRequests";
import ManageModifyUserRequests from "../views/ManageModifyUserRequests";
import Logs from "../views/Logs";
import ViewCertificates from "../views/home/ViewCertificates";
import AdminReport from "../views/AdminReport";

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
        component: ViewPatients,
        name: "ViewPatients",
        path: "/patients",
        beforeEnter: guardRouteDoctor
      },
      {
        component: CSRForm,
        name: "CSRForm",
        path: "/csr",
        beforeEnter: guardRouteAdmin
      },
      {
        component: PublicKeyViewer,
        name: "PublicKeyViewer",
        path: "/key",
        beforeEnter: guardRouteAdmin
      },
      {
        component: ManageDevices,
        name: "ManageDevices",
        path: "/devices",
        beforeEnter: guardRouteAdmin
      },
      {
        component: ManageUsers,
        name: "ManageUsers",
        path: "/users",
        beforeEnter: guardRouteAdmin
      },
      {
        component: ManageAddUserRequests,
        name: "ManageAddUserRequests",
        path: "/add-user-requests",
        beforeEnter: guardRouteAdmin
      },
      {
        component: ManageDeleteUserRequests,
        name: "ManageDeleteUserRequests",
        path: "/delete-user-requests",
        beforeEnter: guardRouteAdmin
      },
      {
        component: ManageModifyUserRequests,
        name: "ManageModifyUserRequests",
        path: "/modify-user-requests",
        beforeEnter: guardRouteAdmin
      },
      {
        component: ViewCertificates,
        name: "ViewCertificates",
        path: "/certificates",
        beforeEnter: guardRouteAdmin
      },
      {
        component: Logs,
        name: "Logs",
        path: "logs",
        beforeEnter: guardRouteAdmin
      },
      {
        component: AdminReport,
        name: "AdminReport",
        path: "adminReport",
        beforeEnter: guardRouteAdmin
      }
    ],
  },
  {
    path: "/404",
    alias: "*",
    component: Error404
  },
];

function guardRouteLoggedIn(to, from, next) {
  let user = JSON.parse(localStorage.getItem("user"));
  if (!user || user["token"] === undefined) next("/login");
  else next(); // allow to enter the route
}

function guardRouteAdmin(to, from, next) {
    let user = JSON.parse(localStorage.getItem('user'));
    if(user['role'] === 'ADMIN')
        next();
}

function guardRouteDoctor(to, from, next) {
    let user = JSON.parse(localStorage.getItem('user'));
    if(user['role'] === 'DOCTOR')
        next();
}

export default new VueRouter({
  mode: "history",
  routes: routes,
});
