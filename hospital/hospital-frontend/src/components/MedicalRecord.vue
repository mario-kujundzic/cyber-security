<template>
  <v-card width="600" height="600px" max-height="700px">
    <v-layout justify-center align-center>
      <v-flex shrink>
        <div>
          <v-img
            mx-auto
            src="../assets/medical-record.jpg"
            width="200"
            height="200"
          ></v-img>
        </div>
      </v-flex>
    </v-layout>

    <div>
      <v-tabs v-model="tab" centered slider-color="blue">
        <v-tab>General</v-tab>
        <v-tab>Details</v-tab>
      </v-tabs>
      <v-tabs-items v-model="tab">
        <v-tab-item>
          <v-card-title class="pb-1">Patient General Information</v-card-title>
          <v-divider class="ml-4 mr-4"></v-divider>
          <v-simple-table class="ml-4 mr-4">
              <tbody>
                <tr>
                  <td><v-icon class="pr-3">mdi-account</v-icon><b>SSID</b></td>
                  <td>{{ record.jmbg }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-account</v-icon><b>Name</b></td>
                  <td>{{ record.name }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-account</v-icon><b>Surname</b></td>
                  <td>{{record.surname}}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-gender-male-female</v-icon><b>Gender</b></td>
                  <td>{{record.gender}}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-calendar-outline</v-icon><b>Birth date</b></td>
                  <td>{{record.dateOfBirth}}</td>
                </tr>
              </tbody>
          </v-simple-table>
        </v-tab-item>
        <v-tab-item>
          <v-card-title class="pb-1">Patient Health Information</v-card-title>
          <v-divider class="ml-4 mr-4"></v-divider>
          <v-simple-table class="ml-4 mr-4">
              <tbody>
                <tr>
                  <td><v-icon class="pr-3">mdi-human-male-height-variant</v-icon><b>Height</b></td>
                  <td>{{ record.height }} cm</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-weight-kilogram</v-icon><b>Weight</b></td>
                  <td>{{ record.weight }} kg</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-water</v-icon><b>Blood pressure</b></td>
                  <td>{{ record.bloodPressure }} mmHg</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-emoticon-sick-outline</v-icon><b>Illness</b></td>
                  <td>{{ record.illness }}</td>
                </tr>
                <tr>
                  <td><v-icon class="pr-3">mdi-needle</v-icon><b>Vaccination</b></td>
                  <td>{{ record.vaccination }}</td>
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
    record: Object,
  },
  methods: {

  },
  watch: {
    record: function() {
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