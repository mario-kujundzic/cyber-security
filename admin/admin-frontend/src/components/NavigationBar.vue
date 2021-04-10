<template>
  <v-container style="width: 350px">
    <v-list>
      <v-list-item>
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
      </v-list-item>

      <v-list-item link>
        <v-list-item-content>
          <v-list-item-title class="title"> Admin Adminovic </v-list-item-title>
          <v-list-item-subtitle>admin1@gmail.com</v-list-item-subtitle>
        </v-list-item-content>

        <v-list-item-action>
          <v-icon>mdi-menu-down</v-icon>
        </v-list-item-action>
      </v-list-item>
    </v-list>
    <v-divider></v-divider>
    <v-list nav dense>
      <v-list-item-group v-model="selectedItem" color="primary">
        <router-link :to="{ name: 'ViewCertificates' }" v-slot="{ navigate }">
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-certificate-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title @click="navigate"
                >Active Certificates</v-list-item-title
              >
            </v-list-item-content>
          </v-list-item>
        </router-link>

        <router-link
          :to="{ name: 'ViewRevokedCertificates' }"
          v-slot="{ navigate }"
        >
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-clipboard-remove-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title @click="navigate"
                >Revoked Certificates</v-list-item-title
              >
            </v-list-item-content>
          </v-list-item>
        </router-link>

        <router-link
          :to="{ name: 'ManageCertificateRequests' }"
          v-slot="{ navigate }"
        >
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-clipboard-text-search-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title @click="navigate"
                >CS Requests</v-list-item-title
              >
            </v-list-item-content>
          </v-list-item>
        </router-link>
      </v-list-item-group>
    </v-list>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    selectedItem: null,
  }),
};
</script>

<style scoped>

</style>
