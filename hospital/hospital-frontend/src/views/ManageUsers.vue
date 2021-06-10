<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col> 
              Users
              <v-btn color="primary" small class="mx-2" @click="onAddClicked">Add user</v-btn>
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
          <v-data-table
            :headers="headers"
            :items="users"
            :search="search"
          >
            <template v-slot:[`item.actions`]="{ item }">
              <v-btn icon small @click="onDeleteClicked(item)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>

            <template v-slot:[`item.fullName`]="{ item }">
                {{item.name}} {{item.surname}}
            </template>

            <template v-slot:[`item.roleChange`]="{ item }">
                <v-btn>
                    Change to {{item.role == 'ADMIN' ? 'USER' : 'ADMIN'}}
                </v-btn>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="addDialog" max-width="50vw">
      <v-card>
        <v-card-title>Add user</v-card-title>
        <v-form v-model="addFormValid">
          <v-card-text>
            <v-container>
              <v-row>
                <v-col>
                  <v-text-field label="Name" v-model="tempCommonName" :rules="commonNameRules"></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field label="Surname" v-model="tempCommonName" :rules="commonNameRules"></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field label="Username" v-model="tempCommonName" :rules="commonNameRules"></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field label="Role" v-model="tempCommonName" :rules="commonNameRules"></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </v-form>

        <v-card-actions class="d-flex justify-end">
          <v-btn color="success" :disabled="!addFormValid" @click="addUser({ commonName: tempCommonName, publicKey: tempPublicKey })">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
        { text: "Role", value: "role" },
        { text: "Request role change", value: "roleChange" },
        { text: "Actions", value: "actions", align: "right"},
      ],
      users: [],
      addDialog: false,
      
      commonNameRules: [
        v => !!v || "Common name is required!"
      ],
      publicKeyRules: [
        v => !!v || "Public key is required!"
      ],

      tempCommonName: "",
      tempPublicKey: "",

      addFormValid: false
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

    updateUser(item) {
      this.axios
      .put(apiURL, item)
      .then((response) => {
        alert(`Successfully updated user ${response.data.commonName}`);
        this.getUsers();
      })
      .catch((error) => {
        alert(error);
      });
    },

    addUser(item) {
      this.axios
      .post(apiURL, item)
      .then((response) => {
        alert(`Successfully added user ${response.data.commonName}`);
        this.getUsers();
      })
      .catch((error) => {
        alert(error);
      });

      this.addDialog = false;
    },

    onAddClicked() {
      this.addDialog = true;
      this.tempCommonName = "";
      this.tempPublicKey = "";
    },

    onDeleteClicked(item) {
      this.axios
      .delete(apiURL + `/${item.id}`)
      .then((response) => {
        alert(response.data.message);
        this.getUsers();
      })
      .catch((error) => {
        alert(error);
      });
    },
  },
};
</script>
