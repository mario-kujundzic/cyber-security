<template>
  <v-container fluid fill-height class="custom-background">
    <v-container class="white">
      <v-row> 
          <img src="../../assets/admin-login.png" />
      </v-row>
      <v-row align="center">
        <ForgotPasswordForm />
      </v-row>
    </v-container>
  </v-container>
</template>

<script>
import ForgotPasswordForm from "../../components/auth/ForgotPasswordForm.vue";
export default {
  name: "ForgotPassword",
  components: { ForgotPasswordForm },
};
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
