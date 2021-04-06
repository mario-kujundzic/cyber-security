<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="10">
        <v-card>
          <v-card-title>
            <v-col> Certificate Requests </v-col>
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
            show-expand
          >
            <template v-slot:[`item.statusChip`]="{ item }">
              <v-chip text-color="white" :color="statusToColor(item.status)">{{
                item.status
              }}</v-chip>
            </template>

            <template v-slot:expanded-item="{ headers, item }">
              <td :colspan="headers.length">
                <div class="ma-10">
                  <v-row>
                    <v-col>
                      <v-text-field
                        :value="item.state"
                        label="State"
                        readonly
                      ></v-text-field>
                    </v-col>

                    <v-col>
                      <v-text-field
                        :value="item.country"
                        label="Country"
                        readonly
                      ></v-text-field>
                    </v-col>

                    <v-col>
                      <v-text-field
                        :value="item.organizationUnit"
                        label="Organization unit"
                        readonly
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col>
                      <v-textarea
                        readonly
                        :value="item.publicKey"
                        label="Public key"
                      ></v-textarea>
                    </v-col>
                  </v-row>
                </div>
              </td>
            </template>

            <template v-slot:[`item.actions`]="{ item }">
              <v-btn icon small @click="approveCSR(item)" class="mr-4">
                <v-icon>mdi-check</v-icon>
              </v-btn>
              <v-btn icon small @click="rejectCSR(item)">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "ManageCertificateRequests",

  data() {
    return {
      search: "",
      headers: [
        { text: "Common Name", value: "commonName" },
        { text: "Email", value: "email" },
        { text: "Locality", value: "locality" },
        { text: "Organization", value: "organization" },
        { text: "Status", value: "statusChip" },
        { text: "Actions", value: "actions" },
        { text: "", value: "data-table-expand" },
      ],
      requests: [],
    };
  },
  mounted() {
    this.axios
      .get("api/certificateRequests")
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

    approveCSR(item) {
      // TODO: Notify hospital admin
      this.$router.push({name: 'AddCertificate', params: {id: item.id}});
    },

    rejectCSR(item) {
      // TODO: Notify hospital admin
      console.log(item);
    },
  },
};
</script>