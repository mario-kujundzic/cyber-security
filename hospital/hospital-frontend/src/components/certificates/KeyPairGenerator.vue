<template>
    <div>
        <h3 style="margin-bottom: 10px">Key pair generator</h3>
        <v-btn color="primary" small @click="generate">Generate</v-btn>

        <div v-if="isGenerating">Please wait, generating keys...</div>

        <div class="mt-5" v-if="privateKey && publicKey">
            <span style="color: red;">Save the private key in a secure place!</span>
            <v-textarea v-model="privateKey" label="Private key"></v-textarea>
            <v-textarea v-model="publicKey" label="Public key"></v-textarea>
        </div>
    </div>
</template>

<script>
import keypair from "keypair";
export default {
    name: 'KeyPairGenerator',
    data() {
        return {
            privateKey: "",
            publicKey: "",
            isGenerating: false
        }
    },

    methods: {
        generate() {
            this.isGenerating = true;
            setTimeout(() => {
                let pair = keypair();
                this.privateKey = pair.private;
                this.publicKey = pair.public;
                this.isGenerating = false;
            }, 100);
        }
    }
}
</script>

<style scoped>

</style>