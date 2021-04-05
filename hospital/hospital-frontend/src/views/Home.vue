<template>
    <div>
        <v-row class="ml-5 mt-5">
            <v-col cols="4">
                <CSRForm></CSRForm>
            </v-col>
            <v-col cols="4">
            </v-col>
        </v-row>
    </div>
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