<template>
    <div>
        <h1>Home page</h1>
        <KeyPairGenerator class ="csr-form"></KeyPairGenerator>
        <CSRForm class="csr-form"></CSRForm>
    </div>
</template>

<script>
import CSRForm from "../components/certificates/CSRForm";
import KeyPairGenerator from "../components/certificates/KeyPairGenerator";
export default {
    name: 'Home',
    components: {
        CSRForm,
        KeyPairGenerator
    }
}
</script>

<style scoped>
.csr-form {
    width: 20%;
    margin-left: 30px;
    margin-bottom: 50px;
}
</style>