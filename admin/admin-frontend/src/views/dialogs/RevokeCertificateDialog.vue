<template>
  <v-dialog
    :value="dialog"
    @input="$emit('update:dialog', false)"
    @keydown.esc="closeDialog()"
    width="400"
  >
    <v-card height="550">
      <v-card-title class="headline">Revoke Certificate</v-card-title>
      <v-card-text class="text--primary">
        Pick a reason for revoking a certificate.
        <div>
          <v-spacer></v-spacer>
          <v-radio-group v-model="reason" column>
            
            <v-tooltip left>
              <template v-slot:activator="{ on, attrs }">
                <v-radio
                  label="Key Compromise"
                  value="KeyCompromise"
                  class="black--text"
                  v-bind="attrs"
                  v-on="on"
                ></v-radio>
              </template>
              <span>A computer is stolen or a smart card is lost.</span>
            </v-tooltip>

            <v-tooltip left>
              <template v-slot:activator="{ on, attrs }">
                <v-radio
                  label="CA Compromise"
                  value="CACompromise"
                  class="black--text"
                  v-bind="attrs"
                  v-on="on"
                ></v-radio>
              </template>
              <span>A CA certificate is compromised.</span>
            </v-tooltip>

            <v-tooltip left>
              <template v-slot:activator="{ on, attrs }">
                <v-radio
                  label="Affiliation Changed"
                  value="AffiliationChanged"
                  class="black--text"
                  v-bind="attrs"
                  v-on="on"
                ></v-radio>
              </template>
              <span>An employee is terminated or suspended.</span>
            </v-tooltip>

            <v-tooltip left>
              <template v-slot:activator="{ on, attrs }">
                <v-radio
                  label="Superseded"
                  value="Superseded"
                  class="black--text"
                  v-bind="attrs"
                  v-on="on"
                ></v-radio>
              </template>
              <span
                >If a smart card fails or the legal name of a user has
                changed.</span
              >
            </v-tooltip>

            <v-tooltip left>
              <template v-slot:activator="{ on, attrs }">
                <v-radio
                  label="Cessation Of Operation"
                  value="CessationOfOperation"
                  class="black--text"
                  v-bind="attrs"
                  v-on="on"
                ></v-radio>
              </template>
              <span>An issued certificate is replaced.</span>
            </v-tooltip>

            <v-tooltip left>
              <template v-slot:activator="{ on, attrs }">
                <v-radio
                  label="Certificate Hold"
                  value="CertificateHold"
                  class="black--text"
                  v-bind="attrs"
                  v-on="on"
                ></v-radio>
              </template>
              <span>A certificate needs to be put on hold temporarily.</span>
            </v-tooltip>

            <v-tooltip left>
              <template v-slot:activator="{ on, attrs }">
                <v-radio
                  label="Remove From CRL"
                  value="RemoveFromCRL"
                  class="black--text"
                  v-bind="attrs"
                  v-on="on"
                ></v-radio>
              </template>
              <span>A CA is removed from the network.</span>
            </v-tooltip>

            <v-radio label="Other" value="Other" class="black--text"></v-radio>
          </v-radio-group>
        </div>
        <p class="overline mb-1 dark-red">
          <v-icon>mdi-alert-outline</v-icon><b>WARNING</b>
          <v-icon>mdi-alert-outline</v-icon>
        </p>
        <p class="text--primary">
          This certificate will be permanently revoked.<br />
          This <b>cannot</b> be undone.
        </p>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="red darken-4" text @click="closeDialog()">
          Revoke
        </v-btn>
        <v-btn color="green darken-10" text @click="closeDialog()">
          Cancel
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "RevokeCertificateDialog",
  data: () => ({
    reason: "Other",
  }),
  props: ["id", "dialog"],
  methods: {
    closeDialog() {
      this.$emit("closeDialog");
    },
  },
};
</script>

<style scoped>
.black--text /deep/ label {
  color: black;
}

.v-tooltip__content {
  opacity: 10 !important;
  background-color: darkred !important;
}

.dark-red {
  color: darkred;
} 
</style>
