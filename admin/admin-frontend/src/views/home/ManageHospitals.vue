<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col> 
              Hospitals
              <v-btn color="primary" small class="mx-2" @click="onAddClicked">Add hospital</v-btn>
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
            :items="hospitals"
            :search="search"
          >
            <template v-slot:[`item.actions`]="{ item }">
              <v-btn icon small @click="onDeleteClicked(item)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>

            <template v-slot:[`item.publicKey`]="{ item }">
              <v-edit-dialog
                :return-value.sync="item.publicKey"
                @save="updateHospital(item)"
              >
                {{ item.publicKey.substring(0, 20) + "..." }}

                <template v-slot:input>
                  <v-text-field
                    v-model="item.publicKey"
                    label="Edit"
                    single-line
                  ></v-text-field>
                </template>
              </v-edit-dialog>
            </template>

            <template v-slot:[`item.commonName`]="{ item }">
              <v-edit-dialog
                :return-value.sync="item.commonName"
                @save="updateHospital(item)"
              >
                {{ item.commonName }}

                <template v-slot:input>
                  <v-text-field
                    v-model="item.commonName"
                    label="Edit"
                    single-line
                  ></v-text-field>
                </template>
              </v-edit-dialog>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="addDialog" max-width="50vw">
      <v-card>
        <v-card-title>Add hospital</v-card-title>
        <v-form v-model="addFormValid">
          <v-card-text>
            <v-container>
              <v-row>
                <v-col>
                  <v-text-field label="Common name" v-model="tempCommonName" :rules="commonNameRules"></v-text-field>
                </v-col>
              </v-row>

              <v-row>
                <v-col>
                  <v-textarea label="Public key" v-model="tempPublicKey" :rules="publicKeyRules"></v-textarea>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </v-form>

        <v-card-actions class="d-flex justify-end">
          <v-btn color="success" :disabled="!addFormValid" @click="addHospital({ commonName: tempCommonName, publicKey: tempPublicKey })">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
        { text: "ID", value: "id" },
        { text: "Common Name", value: "commonName" },
        { text: "Public key", value: "publicKey" },
        { text: "Actions", value: "actions", align: "right"},
      ],
      hospitals: [],
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
    this.getHospitals();
  },

  methods: {
    getHospitals() {
      this.axios
      .get("api/hospitals")
      .then((response) => {
        this.hospitals = response.data;
      })
      .catch((error) => {
        alert(error);
      });
    },

    updateHospital(item) {
      this.axios
      .put("api/hospitals", item)
      .then(() => {
        //alert(`Successfully updated hospital ${response.data.commonName}`);
        this.getHospitals();
      })
      .catch((error) => {
        alert(error);
      });
    },

    addHospital(item) {
      this.axios
      .post("api/hospitals", item)
      .then((response) => {
        alert(`Successfully added hospital ${response.data.commonName}`);
        this.getHospitals();
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
      .delete(`api/hospitals/${item.id}`)
      .then((response) => {
        alert(response.data.message);
        this.getHospitals();
      })
      .catch((error) => {
        alert(error);
      });
    },
  },
};
</script>
