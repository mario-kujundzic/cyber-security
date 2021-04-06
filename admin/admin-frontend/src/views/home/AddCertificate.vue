<template>
    <v-container fluid>
        <v-row align="center" justify="center">
            <v-col cols="8">
                <v-card>
                <v-card-title>
                    Add new certificate
                </v-card-title>
                <add-certificate-form></add-certificate-form>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import AddCertificateForm from "../../components/certificates/AddCertificateForm";
export default {
  name: "Home",
  components: {
    AddCertificateForm,
  },
  data: () => ({}),
};
</script>

<style scoped>
</style>