<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card class="mt-10">
          <v-form v-model="valid" ref="form">
            <v-container fluid>
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
                    prepend-icon="mdi-account"
                    class="pl-3"
                    label="Common Name"
                    v-model="csr.commonName"
                    :rules="[rules.required]"
                    required
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    prepend-icon="mdi-account-group"
                    class="pr-3"
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
                    prepend-icon="mdi-account-group"
                    class="pl-3"
                    label="Organization Unit"
                    v-model="csr.organizationUnit"
                    :rules="[rules.required]"
                    required
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    prepend-icon="mdi-map-marker"
                    class="pr-3"
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
                    prepend-icon="mdi-map-marker"
                    class="pl-3"
                    label="State"
                    v-model="csr.state"
                    :rules="[rules.required]"
                    required
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    prepend-icon="mdi-map-marker"
                    class="pr-3"
                    label="Country code"
                    v-model="csr.country"
                    :rules="[
                      rules.required,
                      rules.uppercaseLetter,
                      rules.maxLetter,
                    ]"
                    required
                  ></v-text-field> </v-col
              ></v-row>
              <v-row class="pl-2 pr-2">
                <v-col>
                  <v-btn block color="indigo accent-1" @click="sendRequest"
                    >Request a certificate</v-btn
                  >
                </v-col>
              </v-row>
            </v-container>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
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

<style scoped></style>
