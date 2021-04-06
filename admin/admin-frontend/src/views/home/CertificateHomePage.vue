<template>
    <v-container fluid>
        <v-row align="center" justify="center">
            <v-col cols="8">
                <v-card>
                    <v-card-title>
                        <v-col>
                            Certificates
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
                        :items="certificates"
                        :search="search"
                    >
                      <template v-slot:[`item.details`]='{item}'>
                        <v-btn icon small @click="log(item)">
                          <v-icon dark>mdi-certificate-outline</v-icon>
                        </v-btn>
                      </template>
                      <template v-slot:[`item.revoke`]='{item}'>
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
  export default {
    data () {
      return {
        search: '',
        headers: [
          { text: 'Serial Number', value: 'serialNumber' },
          { text: 'Common Name', value: 'commonName' },
          { text: 'Issuer', value: 'issuer' },
          { text: 'Date From', value: 'dateFrom' },
          { text: 'Date To', value: 'dateTo' },
          { text: 'Details', value: 'details', filterable: false, sortable: false },
          { text: 'Revoke', value: 'revoke', filterable: false, sortable: false}
        ],
        certificates: [
          {
            serialNumber: 12345678,
            commonName: 'Frozen Yogurt',
            issuer: 'Localhost',
            dateFrom: '06-05-2021',
            dateTo: '06-05-2023',
          },
          {
            serialNumber: 87654321,
            commonName: 'Ice cream sandwich',
            issuer: 'Localhost',
            dateFrom: '06-03-2021',
            dateTo: '06-03-2023',
          },
          {
            serialNumber: 37526285,
            commonName: 'Eclair',
            issuer: 'Localhost',
            dateFrom: '06-03-2021',
            dateTo: '06-03-2023',
          },
        ],
      }
    },
    methods: {
      log(item) {
        console.log(item);
      }
    }
  }
</script>