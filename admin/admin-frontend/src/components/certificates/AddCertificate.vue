<template>
  <v-form v-model="valid" ref="form">
    <v-container>
      <v-row>
        <v-col>
          <v-text-field
            v-model="certificate.commonName"
            :rules="[rules.required]"
            label="Common name"
            required
          ></v-text-field>
        </v-col>
        <v-col>
          <v-text-field
            v-model="certificate.organization"
            :rules="[rules.required]"
            label="Organization"
            required
          ></v-text-field>
        </v-col>
        <v-col>
          <v-text-field
            v-model="certificate.organizationUnit"
            :rules="[rules.required]"
            label="Organization unit"
            required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            v-model="certificate.locality"
            :rules="[rules.required]"
            label="Locality"
            required
          ></v-text-field>
        </v-col>
        <v-col>
          <v-text-field
            v-model="certificate.state"
            :rules="[rules.required]"
            label="State"
            required
          ></v-text-field>
        </v-col>
        <v-col>
          <v-text-field
            v-model="certificate.country"
            :rules="[rules.required, rules.uppercaseLetter, rules.maxLetter]"
            label="Country (2 letter code)"
            required
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-text-field
            v-model="certificate.email"
            :rules="[rules.required]"
            label="Locality"
            required
          ></v-text-field>
        </v-col>
        <v-col>
          <v-menu
            v-model="menuFrom"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="certificate.validFrom"
                label="Valid from"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
                :rules="[rules.required]"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="certificate.validFrom"
              @input="menuFrom = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col cols="12" sm="6" md="4">
          <v-menu
            v-model="menuTo"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="certificate.validTo"
                label="Valid until"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
                :rules="[rules.required]"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="certificate.validTo"
              @input="menuTo = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
      </v-row>
      <v-row class="pt-15">
        <v-col>
          <v-btn color="#8C9EFF" @click="addCert()" block
            >Create certificate</v-btn
          >
        </v-col>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
const apiURL = "/api/certificates";
export default {
  data: () => ({
    valid: false,
    menuFrom: false,
    menuTo: false,
    certificate: {
      commonName: "",
      organization: "",
      organizationUnit: "",
      locality: "",
      state: "",
      country: "",
      email: "",
      validFrom: "",
      validTo: "",
      purpose: [],
    },
    rules: {
      required: (v) => !!v || "Field is required",
      uppercaseLetter: (v) =>
        /[A-Z]{2}/.test(v) ||
        "Country code must be a two-letter uppercase word",
      maxLetter: (v) =>
        v.length < 3 || "Country code must be exactly 2 characters",
    },
  }),
  methods: {
    addCert: function () {
      this.$refs.form.validate();
      if (!this.valid) return;
      const dto = {
        ...this.certificate,
        validFrom: Date.parse(this.certificate.validFrom),
        validTo: Date.parse(this.certificate.validTo),
      };
      console.log("proslo validacije");
      console.log(dto);
      this.axios({
        url: apiURL,
        method: "POST",
        data: dto,
      }).then((response) => {
        console.log(response);
        //this.$router.push({ name: "Home" });
      });
    },
  },
};
</script>