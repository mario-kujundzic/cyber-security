<template>
  <v-card height="50%">
    <v-form v-model="valid" ref="form">
      <v-container fluid >
        <v-row>
          <v-col>
            <v-card-title style="margin-bottom: 10px"
              >Certificate Signing Request</v-card-title
            >
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field
              label="Common Name"
              v-model="csr.commonName"
              :rules="[rules.required]"
            required
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              label="Organization"
              v-model="csr.organization"
              :rules="[rules.required]"
            required
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field
              label="Organization Unit"
              v-model="csr.organizationUnit"
              :rules="[rules.required]"
            required
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              label="Locality"
              v-model="csr.locality"
              :rules="[rules.required]"
            required
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field
              label="State"
              v-model="csr.state"
              :rules="[rules.required]"
            required
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              label="Country code"
              v-model="csr.country"
              :rules="[rules.required, rules.uppercaseLetter, rules.maxLetter]"
            required
            ></v-text-field> </v-col
        ></v-row>
        <v-row>
          <v-col>
            <v-btn block color="primary" @click="sendRequest"
              >Request a certificate</v-btn
            >
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </v-card>
</template>

<script>
export default {
  name: "CSRForm",
  data() {
    return {
      valid: false,
      rules: {
        required: (v) => !!v || "Field is required",
        uppercaseLetter: (v) =>
          /[A-Z]{2}/.test(v) ||
          "Country code must be a two-letter uppercase word",
        maxLetter: (v) =>
          v.length < 3 || "Country code must be exactly 2 characters",
        mail: (v) => /.+@.+\..+/.test(v) || "Email must be a valid email",
      },
      csr: {
        commonName: "",
        organization: "",
        organizationUnit: "",
        locality: "",
        state: "",
        country: "",
      },
    };
  },

  methods: {
    sendRequest() {
      this.$refs.form.validate();
      if (!this.valid) return;

      this.axios
        .post("/api/admin/requestCertificate", this.csr)
        .then((response) => {
          alert(response.data.message);
        })
        .catch((error) => {
          alert(error.response.data.message);
        });
    },
  },
};
</script>

<style scoped>
</style>