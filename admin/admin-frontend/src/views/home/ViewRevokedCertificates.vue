<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col> Revoked certificates </v-col>
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
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "/api/certificates/revoked";

export default {
  name: "ViewCertificates",
  data() {
    return {
      search: "",
      dialog: false,
      headers: [
        { text: "Serial Number", value: "serialNumber" },
        { text: "Common Name", value: "email" },
        { text: "Revocation Reason", value: "revocationReason"},
        { text: "Valid From", value: "validFrom" },
        { text: "Valid To", value: "validTo" },
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
        alert(error);
      });
  }
};
</script>
