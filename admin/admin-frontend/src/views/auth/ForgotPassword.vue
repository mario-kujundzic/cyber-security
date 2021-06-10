<template>
  <v-container fluid fill-height class="custom-background">
    <v-container class="white" style="width: 50%;">
      <v-row justify="center"> 
          <img src="../../assets/forgot.gif" />
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
  height: 300px;
  width: 300px;
}
.custom-background {
  background-color: rgb(119, 99, 211);
}
</style>
