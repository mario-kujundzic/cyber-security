<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-card>
          <v-card-title>
            <v-col> Patients </v-col>
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
            :items="patients"
            :search="search"
          >
          <template v-slot:[`item.record`]="{ item }">
              <v-dialog v-model="recordDialog[item.id]" :retain-focus="false" width="500" height="100%">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    small
                    v-bind="attrs"
                    v-on="on"
                    @click="showMedicalRecord(item)"
                  >
                    <v-icon dark>mdi-medical-bag</v-icon>
                  </v-btn>
                </template>
                <medical-record
                  v-bind:record="recordDetails"
                ></medical-record>
              </v-dialog>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "api/patients";
import MedicalRecord from '../../components/MedicalRecord.vue';

export default {
  name: "ViewPatients",
  components: { MedicalRecord },
  data() {
    return {
      search: "",
      headers: [
        { text: "SSID", value: "jmbg" },
        { text: "Name", value: "name" },
        { text: "Surname", value: "surname" },
        { text: "Birth Date", value: "dateOfBirth" },
        {
          text: "Details",
          value: "record",
          filterable: false,
          sortable: false,
        },
      ],
      patients: [],
      recordDialog: {},
      recordDetails: {}
    };
  },
  mounted() {
    this.axios({
      url: apiURL,
      method: "GET",
    })
      .then((response) => {
        response.data.forEach((r) => {
          r.dateOfBirth = r.dateOfBirth.split("T")[0];
          this.patients.push(r);
        });
      })
      .catch((error) => {
        console.log(error);
      });
  },
  methods: {
    showMedicalRecord(item) {
      this.recordDetails = item;
      this.recordDialog[item.id] = true;
    },
  },
};
</script>
