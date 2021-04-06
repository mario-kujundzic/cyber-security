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
              {{ item.validFrom | formatDate }}
            </template>
            <template v-slot:[`item.validTo`]="{ item }">
              {{ item.validTo | formatDate }}
            </template>
            <template v-slot:[`item.details`]="{ item }">
              <v-btn icon small @click="log(item)">
                <v-icon dark>mdi-certificate-outline</v-icon>
              </v-btn>
            </template>
            <template v-slot:[`item.revoke`]="{ item }">
              <v-btn icon small @click="log(item)">
                <v-icon dark>mdi-close-thick</v-icon>
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
export default {
  name: "ViewCertificates",
  
  data() {
    return {
      search: "",
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
        alert(error);
      });
  },
  methods: {
    log(item) {
      console.log(item);
    },
  },
};
</script>
