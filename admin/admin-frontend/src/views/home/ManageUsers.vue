<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col>
              Users
            </v-col>
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
          <v-data-table :headers="headers" :items="users" :search="search">
            <template v-slot:[`item.fullName`]="{ item }">
              {{ item.name }} {{ item.surname }}
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "api/users";

export default {
  name: "ManageUsers",

  data() {
    return {
      search: "",
      headers: [
        { text: "ID", value: "id" },
        { text: "Full name", value: "fullName" },
        { text: "Username", value: "username" },
        { text: "Hospital", value: "hospital" },
        { text: "Role", value: "role" }
      ],
      users: [],

      rules: {
        required: (v) => !!v || "Field is required!",
        email: (v) =>
          !v ||
          /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
          "E-mail must be in valid format!",
      }
    };
  },

  mounted() {
    this.getUsers();
  },

  methods: {
    getUsers() {
      this.axios
        .get(apiURL)
        .then((response) => {
          this.users = response.data;
        })
        .catch((error) => {
          alert(error);
        });
    },
  },
};
</script>
