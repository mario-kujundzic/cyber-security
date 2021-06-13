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
                <v-btn @click="updateUser(item)" text>
                    Change to {{item.role == 'ADMIN' ? 'DOCTOR' : 'ADMIN'}}
                </v-btn>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="addDialog" max-width="50vw">
      <v-card>
        <v-card-title>Add user</v-card-title>
        <v-form ref="addUserForm" v-model="addFormValid">
          <v-card-text>
            <v-container>
              <v-row>
                <v-col>
                  <v-text-field label="Name" v-model="newUser.name" :rules="[rules.required]"></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field label="Surname" v-model="newUser.surname" :rules="[rules.required]"></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field label="Username" v-model="newUser.username" :rules="[rules.required, rules.email]"></v-text-field>
                </v-col>
                <v-col>
                  <v-select :items="roles" label="Role" v-model="newUser.role" :rules="[rules.required]"></v-select>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </v-form>

        <v-card-actions class="d-flex justify-end">
          <v-btn color="success" :disabled="!addFormValid" @click="addUser()">Submit</v-btn>
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
      
      rules: {
        required: v => !!v || "Field is required!",
        email:  v => !v || /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) || 'E-mail must be in valid format!'        
      },

      newUser: {
        name: "",
        surname: "",
        username: "",
        role: ""
      },

      roles: [
        {
          text: 'Doctor',
          value: 'DOCTOR'
        },
        {
          text: 'Admin',
          value: 'ADMIN'
        }
      ],

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
      .put(apiURL + '/' + item.id)
      .then((response) => {
        alert(`${response.data.message}`);
        this.getUsers();
      })
      .catch((error) => {
        alert(error);
      });
    },

    addUser() {
      this.axios
      .post(apiURL, this.newUser)
      .then((response) => {
        alert(`${response.data.message}`);
        this.getUsers();
        
      })
      .catch((error) => {
        alert(error);
      });
      this.newUser.name = "";
      this.newUser.surname = "";
      this.newUser.username = "";
      this.addDialog = false;
      this.$refs.addUserForm.reset();
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
