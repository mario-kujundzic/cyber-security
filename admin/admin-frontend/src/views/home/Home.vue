<!--template>
  <v-container fluid fill-height class="blue-grey lighten-5">
    <v-app-bar app color="#c2e5cf">
      <v-app-bar-nav-icon></v-app-bar-nav-icon>

      <v-toolbar-title>Cyber Security Admin Application</v-toolbar-title>
      <v-spacer />
      <router-link :to="{ name: 'ManageCertificateRequests' }" v-slot="{navigate}">
        <v-btn @click="navigate" class="ml-3">Requests</v-btn>
      </router-link>
      <router-link :to="{ name: 'ViewCertificates' }" v-slot="{navigate}">
        <v-btn @click="navigate" class="ml-3">Certificates</v-btn>
      </router-link>
      <router-link :to="{ name: 'ViewRevokedCertificates' }" v-slot="{navigate}">
        <v-btn @click="navigate" class="ml-3">Revoked certificates</v-btn>
      </router-link>
    </v-app-bar>

    
    <navigation-bar></navigation-bar>

    <v-main class="container">
      <router-view></router-view>
    </v-main>
  </v-container>
</template-->

<template>
  <v-app>
    <v-navigation-drawer
      v-model="drawer"
      app
    >
      <navigation-bar></navigation-bar>
    </v-navigation-drawer>

    <v-app-bar app color="#c2e5cf">
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>Application</v-toolbar-title>
    </v-app-bar>

    <v-main class="blue-grey lighten-5">
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
import NavigationBar from '../../components/NavigationBar.vue';
//import NavigationBar from '../../components/NavigationBar.vue';
//import NavigationBar from '../../components/NavigationBar.vue';

export default {
  name: "Home",
  components: {NavigationBar },//NavigationBar },
  data: () => ({drawer: null}),
};
</script>

<style scoped>
.container {
  height: 100% !important;
}
</style>