<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="10">
        <v-card>
          <v-card-title>
            <v-col> User Role Requests </v-col>
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
            :items="requests"
            :search="search"
          >
            <template v-slot:[`item.statusChip`]="{ item }">
              <v-chip text-color="white" :color="statusToColor(item.status)">{{
                item.status
              }}</v-chip>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "/api/modifyUserRequests";

export default {
  name: "ManageModifyUserRequests",

  data() {
    return {
      search: "",
      headers: [
        { text: "Name", value: "name" },
        { text: "Surname", value: "surname" },
        { text: "Username", value: "username" },
        { text: "Current role", value: "currentRole" },
        { text: "New role", value: "newRole" },
        { text: "Hospital", value: "hospitalName" },
        { text: "Requested by", value: "adminEmail" },
        { text: "Status", value: "statusChip" },
      ],
      requests: [],
    };
  },
  mounted() {
    this.axios
      .get(apiURL)
      .then((response) => {
        this.requests = response.data;
      })
      .catch((error) => {
        alert(error);
      });
  },
  methods: {
    log(item) {
      console.log(item);
    },

    statusToColor(status) {
      switch (status) {
        case "PENDING":
          return "orange";
        case "SIGNED":
          return "green";
        case "REJECTED":
          return "red";
      }
    },
  },
};
</script>