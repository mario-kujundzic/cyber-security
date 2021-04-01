<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
        <LoginForm/>
    </v-layout>
  </v-container>
</template>

<script>
    import LoginForm from '../components/auth/LoginForm.vue';
    export default {
        name: 'Login',
        components: { LoginForm }
    }
</script>

<style scoped>

</style>