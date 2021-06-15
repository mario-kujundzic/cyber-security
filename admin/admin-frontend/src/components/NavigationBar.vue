<template>
  <v-container>
    <v-layout justify-center align-center>
      <v-flex shrink>
        <v-img
          src="../assets/doggo.gif"
          style="width: 150px; height: 100px"
        ></v-img>
      </v-flex>
    </v-layout>
    <v-list>
      <v-list-item link>
        <v-list-item-content>
          <v-list-item-title class="title d-flex justify-center">{{
            user.name + " " + user.surname
          }}</v-list-item-title>
          <v-list-item-subtitle class="d-flex justify-center">{{
            user.username
          }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <v-divider></v-divider>
    <v-list nav dense>
      <v-list-item-group v-model="selectedItem" color="primary">
        <router-link :to="{ name: 'ViewCertificates' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-certificate-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Active Certificates</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>

        <router-link
          :to="{ name: 'ViewRevokedCertificates' }"
          v-slot="{ navigate }"
        >
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-clipboard-remove-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Revoked Certificates</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>

        <router-link
          :to="{ name: 'ManageCertificateRequests' }"
          v-slot="{ navigate }"
        >
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-clipboard-text-search-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>CS Requests</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <v-divider></v-divider>
        <router-link :to="{ name: 'ManageHospitals' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-hospital-box-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Hospitals</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <v-divider></v-divider>
        <router-link :to="{ name: 'ManageUsers' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-account</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Users</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link :to="{ name: 'ManageAddUserRequests' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-account-plus</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Add User Requests</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link
          :to="{ name: 'ManageDeleteUserRequests' }"
          v-slot="{ navigate }"
        >
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-account-remove</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Delete User Requests</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link
          :to="{ name: 'ManageModifyUserRequests' }"
          v-slot="{ navigate }"
        >
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-account-convert</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Modify User Requests</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link :to="{ name: 'Logs' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-post-outline</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Logs</v-list-item-title>
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
    user: JSON.parse(localStorage["user"]),
  }),
};
</script>

<style scoped>
img {
  height: 50%;
  width: 100%;
}
</style>
