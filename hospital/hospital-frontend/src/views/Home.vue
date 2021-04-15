<template>
    <v-container fluid fill-height class="blue-grey lighten-5">
        <v-row align="center" justify="center">
            <v-col cols="8">
                <CSRForm></CSRForm>
            </v-col>
            <v-col>
                <PublicKeyViewer></PublicKeyViewer>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import CSRForm from "../components/certificates/CSRForm";
import PublicKeyViewer from "../components/certificates/PublicKeyViewer";
export default {
    name: 'Home',
    components: {
        CSRForm,
        PublicKeyViewer
    }
}
</script>

<style scoped>
</style>