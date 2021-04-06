<template>
  <v-content fill-height fluid>
    <v-container class="fill-height"
     fluid>
     <v-row align="center" justify="center">
    <v-card class="mx-auto" max-width="20%">
      <v-img src="../../assets/404-image.jpg" class="justify-center" />
      <v-card-title class="justify-center"> Error 404 </v-card-title>
      <v-card-text class="justify-center headline">
        Sorry, we couldn't find the page you were looking for.
        <br />
        <router-link :to="{ name: 'CertificateHome' }"
          >Return to the home page.
        </router-link>
      </v-card-text>
    </v-card>
     </v-row>
    </v-container>
  </v-content>
</template>