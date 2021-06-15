<template>
  <v-container  fluid>
    <v-row align="start" justify="center">
      <v-col cols="6">
        <v-card>
          <v-card-title> Admin Report </v-card-title>
          <v-card-text>
          <v-menu
            ref="startMenu"
            v-model="startMenu"
            :close-on-content-click="false"
            :return-value.sync="startDate"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="startDate"
                label="Start date"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker v-model="startDate" type="month" no-title scrollable>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="startMenu = false"> Cancel </v-btn>
              <v-btn text color="primary" @click="$refs.startMenu.save(startDate)">
                OK
              </v-btn>
            </v-date-picker>
          </v-menu>
          
          <v-menu
            ref="endMenu"
            v-model="endMenu"
            :close-on-content-click="false"
            :return-value.sync="endDate"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="endDate"
                label="End date"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker v-model="endDate" type="month" no-title scrollable>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="endMenu = false"> Cancel </v-btn>
              <v-btn text color="primary" @click="$refs.endMenu.save(endDate)">
                OK
              </v-btn>
            </v-date-picker>
          </v-menu>

          <v-btn @click="getReport()" class="mb-5">Get report </v-btn> 

          <v-alert class="mb-5" type='info' v-if="requested && !(report['INFO'] || report['WARNING'] || report['ERROR'])">
            No logs for that time period. Please enter another time frame.
          </v-alert>
          <v-alert class="mb-5" type='success' v-if="report['INFO']">
            INFO logs: There has been a total of {{report['INFO'].length}} success logs! 
          </v-alert>
          
          <v-alert class="mb-5" type='warning' v-if="report['WARNING']">
            WARNING logs: There has been a total of {{report['WARNING'].length}} warning logs! 
          </v-alert>
          
          <v-alert type='error' v-if="report['ERROR']">
            ERROR logs: There has been a total of {{report['ERROR'].length}} error logs! 
          </v-alert>
          </v-card-text>

        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "api/logs/report";

export default {
  name: "AdminReport",

  data() {
    return {
      startMenu: false,
      startDate: new Date(0).toISOString().substring(0, 10),
      endMenu: false,
      endDate: new Date().toISOString().substring(0, 10),
      report: {},
      requested: false
    };
  },

  methods: {
    getReport() {
      const startDateParsed = new Date(this.startDate);
      const endDateParsed = new Date(this.endDate);
      this.axios
        .get(apiURL + "?since=" + startDateParsed.getTime() + "&until=" + endDateParsed.getTime())
        .then((response) => {
          this.report = response.data;
          this.requested = true;
        })
        .catch((error) => {
          alert(error.response.data.message);
        });
    },
  },
};
</script>

<style scoped>
</style>
