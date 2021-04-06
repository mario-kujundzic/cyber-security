<template>
  <v-container fluid fill-height class="custom-background">
    <v-container id="login-card" class="white" width="70%" height="70%">
        <v-row>
            <v-col 
                no-gutters 
                cols="12"
                md
            >
                <img src="../../assets/admin-login.png">
            </v-col>
            <v-col 
                no-gutters 
                cols="12"
                md
            >
                <LoginForm/>   
            </v-col>
        </v-row>
    </v-container>
        
  </v-container>
</template>

<script>
    import LoginForm from '../../components/auth/LoginForm.vue';
    export default {
        name: 'Login',
        components: { LoginForm }
    }
</script>

<style scoped>
    img {
        height: 100%;
        width: 100%;
    }
    .custom-background {
        background-color: #c2e5cf;
    }

</style>