<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" app>
      <navigation-bar></navigation-bar>
    </v-navigation-drawer>

    <v-app-bar app color="#c2e5cf">
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>Admin Application</v-toolbar-title>
      <v-spacer />

      <router-link :to="{ name: 'ManageHospitals' }" v-slot="{navigate}">
        <v-btn @click="navigate" class="ml-3">Hospitals</v-btn>
      </router-link>
    </v-app-bar>

    <v-main class="blue-grey lighten-5">
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
import NavigationBar from "../../components/NavigationBar.vue";

export default {
  name: "Home",
  components: { NavigationBar },
  data: () => ({ drawer: false }),
};
</script>

<style scoped>
.container {
  height: 100% !important;
}
</style>
