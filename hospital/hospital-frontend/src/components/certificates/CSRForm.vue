<template>
  <v-card width="40%" height="50%">
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
              label="Common Name/Email"
              v-model="csr.commonName"
              :rules="[rules.required, rules.mail]"
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
              v-model="csr.cityLocality"
              :rules="[rules.required]"
            required
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field
              label="State"
              v-model="csr.stateProvince"
              :rules="[rules.required]"
            required
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
              label="Country code"
              v-model="csr.countryRegion"
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
        cityLocality: "",
        stateProvince: "",
        countryRegion: "",
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
          alert("Oops, an error occurred!\n" + error);
        });
    },
  },
};
</script>

<style scoped>
</style>