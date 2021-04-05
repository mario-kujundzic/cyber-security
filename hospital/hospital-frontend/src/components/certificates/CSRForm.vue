<template>
    <div>
        <h3 style="margin-bottom: 10px">Certificate Signing Request</h3>
        <v-form>
            <v-text-field label="Common Name" v-model="csr.commonName"></v-text-field>
            <v-text-field label="Organization" v-model="csr.organization"></v-text-field>
            <v-text-field label="Organization Unit" v-model="csr.organizationUnit"></v-text-field>
            <v-text-field label="City/Locality" v-model="csr.cityLocality"></v-text-field>
            <v-text-field label="State/Province" v-model="csr.stateProvince"></v-text-field>
            <v-text-field label="Country/Region" v-model="csr.countryRegion"></v-text-field>
            <v-textarea label="Public key" v-model="csr.publicKey"></v-textarea>
        </v-form>

        <v-btn color="primary" small @click="sendRequest">Request a certificate</v-btn>
    </div>
</template>

<script>
export default {
    name: 'CSRForm',
    data() {
        return {
            csr: {
                commonName: "",
                organization: "",
                organizationUnit: "",
                cityLocality: "",
                stateProvince: "",
                countryRegion: "",
                publicKey: ""
            }
        }
    },

    methods: {
        sendRequest() {

        }
    }
}
</script>

<style scoped>

</style>