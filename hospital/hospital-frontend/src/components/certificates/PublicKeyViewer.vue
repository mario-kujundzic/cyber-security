<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-card class="mt-10" style="height: 400px;">
          <v-card-title centered class="pl-5">Public Key </v-card-title>
          <v-textarea
            class="pl-5 pr-5"
            v-model="publicKey"
            readonly
            rows="8"
          ></v-textarea>
          <v-card-actions>
            <v-container class="pl-2 pr-2">
              <v-btn block color="indigo accent-1" @click="onCopyClicked">
                COPY
              </v-btn>
            </v-container>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
// style="float: left;"
import copy from "copy-to-clipboard";
export default {
  name: "PublicKeyViewer",
  data() {
    return {
      publicKey: "",
    };
  },

  async mounted() {
    try {
      let response = await this.axios.get("/api/publicKey");
      this.publicKey = response.data;
    } catch (error) {
      alert("Can't get public key: " + error);
    }
  },

  methods: {
    onCopyClicked() {
      copy(this.publicKey);
    },
  },
};
</script>

<style scoped></style>
