<template>
    <v-container fluid fill-height class="blue-grey lighten-5">
        <v-row align="center" justify="center">
        <CSRForm></CSRForm>
        </v-row>
    </v-container>
</template>

<script>
import CSRForm from "../components/certificates/CSRForm";
export default {
    name: 'Home',
    components: {
        CSRForm
    }
}
</script>

<style scoped>
</style>