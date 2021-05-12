<template>
  <v-form v-model="valid" ref="form">
    <v-container>
      <v-row>
        <v-col>
          <v-autocomplete
            v-model="chosenTemplate"
            :items="templates"
            label="Template"
          ></v-autocomplete>
        </v-col>
      </v-row>
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
            :rules="[rules.required, rules.mail]"
            label="Email"
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
              @change="setTemplateCustom"
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
              @change="setTemplateCustom"
            ></v-date-picker>
          </v-menu>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-autocomplete
            v-model="certificate.purpose"
            :items="certPurposes"
            :rules="[rules.requiredArray]"
            label="Certificate purpose"
            multiple
            @change="setTemplateCustom"
          ></v-autocomplete>
        </v-col>
        <v-col>
          <v-autocomplete
            v-model="certificate.algorithm"
            :items="cryptAlgorithms"
            :rules="[rules.required]"
            label="Crypting algorithm"
            @change="setTemplateCustom"
          ></v-autocomplete>
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
import moment from "moment";

const apiCert = "/api/certificates";
const apiReq = "/api/certificateRequests";
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
      algorithm: "",
      requestId: 1,
    },
    request: {},
    certPurposes: [
      { text: "Digital signature", value: "128" },
      { text: "CRL sign", value: "2" },
      { text: "Data encipherment", value: "16" },
      { text: "Decipher only", value: "32768" },
      { text: "Encipher only", value: "1" },
      { text: "Key agreement", value: "8" },
      { text: "Key certificate sign", value: "4" },
      { text: "Key encipherment", value: "32" },
      { text: "Non repudiation", value: "64" },
    ],
    cryptAlgorithms: [
      { text: "SHA 256 with RSA", value: "SHA256WithRSAEncryption" },
      { text: "SHA 512 with RSA", value: "SHA512WithRSAEncryption" },
    ],
    chosenTemplate: { text: "Custom", value: { custom: true } },
    templates: [
      {
        text: "Custom",
        value: { custom: true },
      },
      {
        text: "Digital Signature and CRL Sign with SHA256, for 6 months",
        value: {
          time: "6 months",
          purpose: ["128", "2"],
          algorithm: "SHA256WithRSAEncryption",
          custom: false,
        },
      },
      {
        text: "Digital Signature and Data encipherment with SHA512, for 1 year",
        value: {
          time: "1 year",
          purpose: ["128", "16"],
          algorithm: "SHA512WithRSAEncryption",
          custom: false,
        },
      },
    ],
    rules: {
      required: (v) => !!v || "Field is required",
      requiredArray: (v) => v.length != 0 || "Field is required",
      uppercaseLetter: (v) =>
        /[A-Z]{2}/.test(v) ||
        "Country code must be a two-letter uppercase word",
      maxLetter: (v) =>
        v.length < 3 || "Country code must be exactly 2 characters",
      mail: (v) => /.+@.+\..+/.test(v) || "Email must be a valid email",
    },
  }),
  mounted: function () {
    if (!this.$route.params.id) {
      this.$router.push({ name: "ViewCertificates" });
    }
    this.axios({
      url: apiReq + "/" + this.$route.params.id,
      method: "GET",
    })
      .then((response) => {
        this.request = response.data;
        this.certificate.requestId = this.$route.params.id;
        this.certificate.commonName = this.request.commonName;
        this.certificate.organization = this.request.organization;
        this.certificate.organizationUnit = this.request.organizationUnit;
        this.certificate.locality = this.request.locality;
        this.certificate.state = this.request.state;
        this.certificate.country = this.request.country;
        this.certificate.email = this.request.email;
      })
      .catch(() => {
        this.$router.push({ name: "ViewCertificates" });
      });
  },
  methods: {
    addCert: function () {
      alert(this.certificate.validFrom);
      this.$refs.form.validate();
      if (!this.valid) return;
      const dto = {
        ...this.certificate,
        validFrom: Date.parse(this.certificate.validFrom),
        validTo: Date.parse(this.certificate.validTo),
      };
      this.axios({
        url: apiCert,
        method: "POST",
        data: dto,
      }).then(() => {
        this.$router.push({ name: "ViewCertificates" });
      });
    },
    setTemplateCustom: function () {
      this.chosenTemplate = { custom: true };
    },
  },
  watch: {
    chosenTemplate: function (newTemplate) {
      if (!newTemplate.custom) {
        if (newTemplate.time == "1 year") {
          this.certificate.validFrom = moment().format("YYYY-MM-DD");
          this.certificate.validTo = moment()
            .add(1, "years")
            .format("YYYY-MM-DD");
        }
        if (newTemplate.time == "6 months") {
          this.certificate.validFrom = moment().format("YYYY-MM-DD");
          this.certificate.validTo = moment()
            .add(6, "months")
            .format("YYYY-MM-DD");
        }
        this.certificate.purpose = newTemplate.purpose;
        this.certificate.algorithm = newTemplate.algorithm;
      }
    },
  },
};
</script>