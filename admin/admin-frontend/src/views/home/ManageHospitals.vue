<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col> Hospitals </v-col>
            <v-col>
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Search"
                single-line
                hide-details
              ></v-text-field>
            </v-col>
          </v-card-title>
          <v-data-table
            :headers="headers"
            :items="hospitals"
            :search="search"
          >
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
//const apiURL = "/api/hospitals";

export default {
  name: "ManageHospitals",

  data() {
    return {
      search: "",
      headers: [
      ],
      hospitals: [],
    };
  },
  mounted() {

  },
  methods: {

  },
};
</script>
