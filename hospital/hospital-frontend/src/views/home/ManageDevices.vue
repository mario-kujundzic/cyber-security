<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col> 
              Devices
              <v-btn color="primary" small class="mx-2" @click="onAddClicked">Add device</v-btn>
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
            :items="devices"
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
                @save="updateDevice(item)"
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
                @save="updateDevice(item)"
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
            
            <template v-slot:[`item.certRequest`]="{ item }">
                <v-btn @click="requestCert(item)" text>
                    Request certificate
                </v-btn>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="addDialog" max-width="50vw">
      <v-card>
        <v-card-title>Add device</v-card-title>
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
          <v-btn color="success" :disabled="!addFormValid" @click="addDevice({ commonName: tempCommonName, publicKey: tempPublicKey })">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
const apiURL = "api/devices";

export default {
  name: "ManageDevices",

  data() {
    return {
      search: "",
      headers: [
        { text: "ID", value: "id" },
        { text: "Common Name", value: "commonName" },
        { text: "Public key", value: "publicKey" },
        { text: "Request certificate", value: "certRequest" },
        { text: "Actions", value: "actions", align: "right"},
      ],
      devices: [],
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
    this.getDevices();
  },

  methods: {
    getDevices() {
      this.axios
      .get(apiURL)
      .then((response) => {
        this.devices = response.data;
      })
      .catch((error) => {
        alert(error);
      });
    },

    updateDevice(item) {
      this.axios
      .put(apiURL, item)
      .then((response) => {
        alert(`Successfully updated device ${response.data.commonName}`);
        this.getDevices();
      })
      .catch((error) => {
        alert(error);
      });
    },

    addDevice(item) {
      this.axios
      .post(apiURL, item)
      .then((response) => {
        alert(`Successfully added device ${response.data.commonName}`);
        this.getDevices();
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
        this.getDevices();
      })
      .catch((error) => {
        alert(error);
      });
    },

    requestCert(item) {
      const csr = {
        commonName: item.commonName,
        hospitalName: item.commonName,
        organization: "Cyber Security Hospital Center",
        organizationUnit: "Cyber Security Administrative Center",
        locality: "Novi Sad",
        state: "Vojvodina",
        country: "RS",
      };
      this.axios
        .post("/api/devices/requestCertificate", csr)
        .then((response) => {
          alert(response.data.message);
        })
        .catch((error) => {
          let errorMessage = error.response.data.message;
          alert(errorMessage);
        });
    }
  },
};
</script>
