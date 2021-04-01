<template>
  <v-container fluid fill-height class="indigo accent-1">
    <v-container id="login-card" class="white" width="70%" height="70%">
        <v-row>
            <v-col 
                no-gutters 
                cols="12"
                md
            >
                <img src="../assets/doctor.png">
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
    import LoginForm from '../components/auth/LoginForm.vue';
    export default {
        name: 'Login',
        components: { LoginForm }
    }
</script>

<style scoped>
    /*.v-container {
        color: #7986CB;
    }
    .v-layout {
        background-color: #7986CB;
    }
    .v-application--wrap {
        background-color: #7986CB;
    }*/
    img {
        height: 100%;
        width: 100%;
    }

</style>