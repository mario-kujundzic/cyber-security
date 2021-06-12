<template>
  <v-content fill-height fluid>
    <v-container class="fill-height blue-grey lighten-5" fluid>
      <v-row align="center" justify="center">
        <v-card class="mx-auto" max-width="30%">
          <v-img
            src="../../assets/sad-doggo.gif"
            class="center"
            style="height: 50%; width: 100%;"
          />
          <v-card-title
            class="justify-center description"
            style="font-size: 50px;"
          >
            <b>Error 404</b>
          </v-card-title>
          <v-card-text class="justify-center description mt-8" style="font-size: 25px;">
            Sorry, we couldn't find the page you were looking for.
            <br />
            <router-link style="text-decoration: none;" :to="{ name: 'ViewCertificates' }"
              ><p class="mt-8" align="center" >Return to the home page.</p>
            </router-link>
          </v-card-text>
        </v-card>
      </v-row>
    </v-container>
  </v-content>
</template>

<style>
.description {
  font-family: "Baloo2", Helvetica, Arial;
}
</style>
