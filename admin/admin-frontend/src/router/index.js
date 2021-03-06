import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

import Login from "../views/auth/Login";
import Home from "../views/home/Home";
import AddCertificate from "../views/home/AddCertificate";
import ViewCertificates from "../views/home/ViewCertificates";
import ViewRevokedCertificates from "../views/home/ViewRevokedCertificates";
import Error404 from "../views/errors/Error404"
import ManageCertificateRequests from "../views/home/ManageCertificateRequests";
import ManageHospitals from "../views/home/ManageHospitals";
import ManageAddUserRequests from "../views/home/ManageAddUserRequests";
import ManageDeleteUserRequests from "../views/home/ManageDeleteUserRequests";
import ManageModifyUserRequests from "../views/home/ManageModifyUserRequests";
import ForgotPassword from '../views/auth/ForgotPassword';
import ResetPassword from '../views/auth/ResetPassword';
import Logs from "../views/Logs";
import ManageUsers from '../views/home/ManageUsers';

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
    path: "/reset-password/:key"
  },
  {
    component: Home,
    path: "/",
    beforeEnter: guardRouteLoggedIn,
    children: [
      {
        component: ViewCertificates,
        name: "ViewCertificates",
        path: "",
      },
      {
        component: ViewRevokedCertificates,
        name: "ViewRevokedCertificates",
        path: "revoked",
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
      {
        component: ManageHospitals,
        name: "ManageHospitals",
        path: "hospitals",
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
      {
        component: Logs,
        name: "Logs",
        path: "logs"
      },
      {
        component: ManageUsers,
        name: "ManageUsers",
        path: "users",
      },
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
  if (!user || !user["token"]) next("/login");
  else next(); // allow to enter the route
}

/*function guardRouteAdmin(to, from, next) {
    let user = JSON.parse(localStorage.getItem('user'));
    if(user['role'] === 'ADMIN')
        next();
}*/

/*function guardRouteDoctor(to, from, next) {
    let user = JSON.parse(localStorage.getItem('user'));
    if(user['role'] === 'DOCTOR')
        next();
}*/

export default new VueRouter({
  mode: "history",
  routes: routes,
});
