<template>
  <v-container fluid fill-height class="white">
    <v-app-bar app class="indigo accent-1">
      <v-app-bar-nav-icon></v-app-bar-nav-icon>

      <v-toolbar-title>Cyber Security Admin Application</v-toolbar-title>
    </v-app-bar>

    <v-main>
      <add-certificate></add-certificate>
    </v-main>
  </v-container>
</template>

<script>
import AddCertificate from '../components/certificates/AddCertificate'
export default {
  name: "Home",
  components: {
      AddCertificate
  },
  data: () => (
      {
          
      }
  )
};
</script>

<style scoped>
</style>