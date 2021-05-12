<template>
  <v-card>
    <v-card-title> Certificate details </v-card-title>
    <v-form>
      <v-container>
        <v-row>
          <v-col>
            <v-text-field
              :value="certificate.commonName"
              label="Common name"
              readonly
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              :value="certificate.organization"
              label="Organization"
              readonly
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              :value="certificate.organizationUnit"
              label="Organization unit"
              readonly
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field
              :value="certificate.locality"
              label="Locality"
              readonly
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              :value="certificate.state"
              label="State"
              readonly
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              :value="certificate.country"
              label="Country"
              readonly
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field
              :value="certificate.email"
              label="Email"
              readonly
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              label="Valid from"
              :value="certificate.validFrom.toDateString()"
              prepend-icon="mdi-calendar"
              readonly
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              :value="certificate.validTo.toDateString()"
              label="Valid until"
              prepend-icon="mdi-calendar"
              readonly
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field
              :value="certificate.purposeReadable.join(', ')"
              label="Certificate purpose"
              readonly
            >
            </v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              v-model="certificate.algorithm"
              label="Crypting algorithm"
              required
            ></v-text-field>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </v-card>
</template>

<script>
export default {
  data: () => ({}),
  props: {
    certificate: Object,
  },
};
</script>

<style scoped></style>
