<template>
  <v-card width="600" height="100%">
    <v-layout justify-center align-center>
      <v-flex shrink>
        <div>
          <v-img
            mx-auto
            src="../../assets/certificate-icon.jpg"
            width="200"
            height="200"
          ></v-img>
        </div>
      </v-flex>
    </v-layout>

    <div class="tab-wrapper">
      <v-tabs v-model="tab" centered slider-color="blue">
        <v-tab>General</v-tab>
        <v-tab>Details</v-tab>
        <!--v-tab>Certification Path</v-tab-->
      </v-tabs>
      <v-tabs-items v-model="tab">
        <v-tab-item>
          <v-card-title class="pb-1">Certificate Information</v-card-title>
          <v-divider class="ml-4 mr-4"></v-divider>
          <v-simple-table class="ml-4 mr-4">
              <tbody>
                <tr>
                  <td><v-icon class="pr-3">mdi-account</v-icon><b>Issued To</b></td>
                  <td>{{ certificate.commonName }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-certificate</v-icon><b>Issued By</b></td>
                  <td>{{ certificate.issuer }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-calendar-outline</v-icon><b>Valid From</b></td>
                  <td>{{certificate.validFrom.toDateString()}}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-calendar-outline</v-icon><b>Valid To</b></td>
                  <td>{{certificate.validTo.toDateString()}}</td>
                </tr>
              </tbody>
          </v-simple-table>
          <v-list
            subheader
            class="ml-4 mr-4"
          >
            <v-subheader class="mb-0 pb-0"><b>This certificate is intended for the following purpose(s):</b></v-subheader>
            <v-list-item
              class="pt-0"
              v-for="purpose in certificate.purposeReadable"
              :key="purpose"
            >
              {{purpose}}</v-list-item>
          </v-list>
        </v-tab-item>
        <v-tab-item>
          <v-card-title class="pb-1">Certificate Details</v-card-title>
          <v-simple-table class="ml-4 mr-4">
              <tbody>
                <tr>
                  <td><v-icon class="pr-3">mdi-email-edit</v-icon><b>Email</b></td>
                  <td>{{ certificate.email }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-account-group</v-icon><b>Organization</b></td>
                  <td>{{ certificate.organization }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-account-group</v-icon><b>Organization Unit</b></td>
                  <td>{{ certificate.organizationUnit }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-map-marker</v-icon><b>Locality</b></td>
                  <td>{{certificate.locality}}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-map-marker</v-icon><b>State</b></td>
                  <td>{{certificate.state}}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-map-marker</v-icon><b>Country</b></td>
                  <td>{{certificate.country}}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-shield-lock</v-icon><b>Crypting algorithm</b></td>
                  <td>{{certificate.algorithm}}</td>
                </tr>
              </tbody>
          </v-simple-table>
        </v-tab-item>
      </v-tabs-items>
    </div>
  </v-card>
</template>

<script>
export default {
  data: () => ({
    tab: 0,
  }),
  props: {
    certificate: Object,
  },
  methods: {

  },
  watch: {
    certificate: function() {
      this.tab = 0;
    }
  }
};
</script>

<style scoped>
.tab-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.tab-wrapper .v-window {
  flex: 1;
}

.tab-wrapper .v-window__container,
.tab-wrapper .v-window-item {
  height: 450px !important;
}

.v-window__container {
  height: 500px !important;
  position: relative;
}

.v-window-item .v-window-item--active {
  height: 500px !important;
}

.tab-wrapper .v-card {
  height: 500px !important;
}

.theme--light.v-subheader {
    color: black!important;
}
</style>
