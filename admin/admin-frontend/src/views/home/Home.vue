<template>
  <v-container fluid fill-height class="blue-grey lighten-5">
    <v-app-bar app color="#c2e5cf">
      <v-app-bar-nav-icon></v-app-bar-nav-icon>

      <v-toolbar-title>Cyber Security Admin Application</v-toolbar-title>
      <v-spacer />
      <router-link :to="{ name: 'AddCertificate' }" v-slot="{navigate}">
        <v-btn @click="navigate" class="ml-3">Add certificate</v-btn>
      </router-link>
      <router-link :to="{ name: 'ViewCertificates' }" v-slot="{navigate}">
        <v-btn @click="navigate" class="ml-3">View certificates</v-btn>
      </router-link>
    </v-app-bar>

    <v-main>
      <router-view></router-view>
    </v-main>
  </v-container>
</template>

<script>
export default {
  name: "Home",
  components: {},
  data: () => ({}),
};
</script>

<style scoped>
</style>