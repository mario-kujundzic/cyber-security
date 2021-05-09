<template>
    <div>
        <v-btn icon style="float: left;" @click="onCopyClicked">
            <v-icon>
                mdi-content-copy
            </v-icon>
        </v-btn>
        <v-textarea v-model="publicKey" label="Public key" readonly></v-textarea>
    </div>
</template>

<script>
import copy from "copy-to-clipboard";
export default {
    name: 'PublicKeyViewer',
    data() {
        return {
            publicKey: ""
        }
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
        }
    }
}
</script>

<style scoped>

</style>