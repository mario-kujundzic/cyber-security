<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="10">
        <v-card>
          <v-card-title>
            <v-col> Certificates </v-col>
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
            :items="certificates"
            :search="search"
          >
            <template v-slot:[`item.validFrom`]="{ item }">
              {{ new Date(item.validFrom).toDateString() }}
            </template>
            <template v-slot:[`item.validTo`]="{ item }">
              {{ new Date(item.validTo).toDateString() }}
            </template>
            <template v-slot:[`item.revocationStatus`]="{ item }">
              {{ item.revocationStatus ? "Yes" : "No" }}
            </template>
            <template v-slot:[`item.revocationReason`]="{ item }">
              {{
                item.revocationStatus ? item.revocationReason : "Not revoked"
              }}
            </template>
            <template v-slot:[`item.revoke`]="{ item }">
              <v-dialog
                v-model="revokeDialog"
                :retain-focus="false"
                width="400"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    small
                    :disabled="item.rootAuthority"
                    v-bind="attrs"
                    v-on="on"
                    @click="revokeCertificate(item)"
                  >
                    <v-icon dark>mdi-close-thick</v-icon>
                  </v-btn>
                </template>
                <revoke-certificate
                  v-on:remove-certificate="removeCertificate"
                  v-on:close-revoke-dialog="revokeDialog = false"
                  v-bind:serialNumber="serialNumber"
                >
                </revoke-certificate>
              </v-dialog>
            </template>
            <template v-slot:[`item.status`]="{ item }">
              <v-btn icon small @click="checkStatus(item)" :disabled="item.revocationStatus">
                <v-icon dark>mdi-list-status</v-icon>
              </v-btn>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "api/certificates";
import RevokeCertificate from "../../components/certificates/RevokeCertificateForm.vue";

export default {
  name: "ViewCertificates",
  components: { RevokeCertificate },
  data() {
    return {
      search: "",
      headers: [
        { text: "Serial number", value: "serialNumber" },
        { text: "Common name", value: "commonName" },
        { text: "Type of certificate", value: "certificateUser" },
        { text: "Valid from", value: "validFrom" },
        { text: "Valid to", value: "validTo" },
        { text: "Email", value: "email" },
        { text: "Revoked", value: "revocationStatus" },
        { text: "Revocation reason", value: "revocationReason" },
        { text: "Revoke", value: "revoke" },
        {
          text: "Status",
          value: "status",
          filterable: false,
          sortable: false,
        },
      ],
      certificates: [],
      revokeDialog: false,
      serialNumber: "",
    };
  },
  mounted() {
    this.axios({
      url: apiURL,
      method: "GET",
    })
      .then((response) => {
        this.certificates = response.data;
      })
      .catch((error) => {
        alert(error.response.data);
        console.log(error);
      });
  },
  methods: {
    revokeCertificate(item) {
      if (item.rootAuthority) {
        return;
      }
      this.revokeDialog = true;
      this.serialNumber = item.serialNumber;
    },
    removeCertificate(cert) {
      this.certificates.forEach((c) => {
        if (c.id == cert.id) {
          c.revocationStatus = cert.revocationStatus;
          c.revocationReason = cert.revocationReason;
          c.validTo = cert.validTo;
        }
      });
      this.revokeDialog = false;
    },
    checkStatus(item) {
      this.axios
        .get(apiURL + "/status/" + item.serialNumber)
        .then((response) => {
          this.certificates.forEach((c) => {
            if (c.id === item.id) {
              c.revocationStatus = response.data.revocationStatus;
              c.revocationReason = response.data.revocationReason;
              c.validTo = response.data.validTo;
            }
          });
          if (response.data.revocationStatus) {
            alert(
              "Certificate is revoked! Revocation reason: " +
                response.data.revocationReason
            );
          } else {
            alert("Certificate is valid!");
          }
        })
        .catch((error) => {
          alert(error.response.data.message);
        });
    },
  },
};
</script>
