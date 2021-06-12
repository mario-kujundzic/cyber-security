<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
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
              {{ item.validFrom.toDateString() }}
            </template>
            <template v-slot:[`item.validTo`]="{ item }">
              {{ item.validTo.toDateString() }}
            </template>
            <template v-slot:[`item.details`]="{ item }">
              <v-dialog v-model="detailsDialog" :retain-focus="false" width="600" height="100%">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    small
                    v-bind="attrs"
                    v-on="on"
                    @click="showCertDetails(item)"
                  >
                    <v-icon dark>mdi-certificate-outline</v-icon>
                  </v-btn>
                </template>
                <view-certificate-form
                  v-bind:certificate="certDetails"
                ></view-certificate-form>
              </v-dialog>
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
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import ViewCertificateForm from "../../components/certificates/ViewCertificateForm.vue";
import RevokeCertificate from "../../components/certificates/RevokeCertificateForm.vue";
const apiURL = "/api/certificates";

export default {
  name: "ViewCertificates",
  components: { ViewCertificateForm, RevokeCertificate },
  data() {
    return {
      search: "",
      detailsDialog: false,
      revokeDialog: false,
      serialNumber: "",
      certDetails: "",
      headers: [
        { text: "Serial Number", value: "serialNumber" },
        { text: "Common Name", value: "commonName" },
        { text: "Issuer", value: "issuer" },
        { text: "Valid From", value: "validFrom" },
        { text: "Valid To", value: "validTo" },
        {
          text: "Details",
          value: "details",
          filterable: false,
          sortable: false,
        },
        { text: "Revoke", value: "revoke", filterable: false, sortable: false },
      ],
      certificates: [],
    };
  },
  mounted() {
    this.axios({
      url: apiURL,
      method: "GET",
    })
      .then((response) => {
        response.data.forEach((r) => {
          r.validFrom = new Date(r.validFrom);
          r.validTo = new Date(r.validTo);
          this.certificates.push(r);
        });
      })
      .catch((error) => {
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
    removeCertificate(serialNumber) {
      this.certificates = this.certificates.filter(
        (f) => f.serialNumber != serialNumber
      );
      this.revokeDialog = false;
    },
    showCertDetails(item) {
      this.certDetails = item;
      this.detailsDialog = true;
    },
  },
};
</script>