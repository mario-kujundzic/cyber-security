<template>
  <v-container>
    <v-layout justify-center align-center>
      <v-flex shrink>
        <v-img
          src="../assets/doggo.gif"
          style="width: 100px; height: 100px"
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
    <v-list v-if="user.role === 'ADMIN'" nav dense>
      <v-list-item-group v-model="selectedItem" color="primary">
        <router-link :to="{ name: 'CSRForm' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-certificate-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>CS Request</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link :to="{ name: 'PublicKeyViewer' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-key-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Public Key Viewer</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <v-divider></v-divider>
        <router-link :to="{ name: 'ViewCertificates' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-certificate-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Certificates</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link :to="{ name: 'ManageDevices' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-gauge</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Devices</v-list-item-title>
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
        <router-link
          :to="{ name: 'ManageAddUserRequests' }"
          v-slot="{ navigate }"
        >
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
      </v-list-item-group>
    </v-list>
    <v-list v-if="user.role === 'DOCTOR'" nav dense>
      <v-list-item-group v-model="selectedItem" color="primary">
        <router-link :to="{ name: 'ViewPatients' }" v-slot="{ navigate }">
          <v-list-item @click="navigate">
            <v-list-item-icon>
              <v-icon>mdi-medical-bag</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Medical Records</v-list-item-title>
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

<style scoped></style>
