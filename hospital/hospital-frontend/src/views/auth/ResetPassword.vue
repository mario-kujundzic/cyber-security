<template>
  <v-container fluid fill-height class="indigo accent-1">
    <v-container class="white" style="width: 50%;">
      <v-row justify="center"> 
          <img src="../../assets/reset-pass.jpg" />
      </v-row>
      <v-row align="center">
        <ResetPasswordForm />
      </v-row>
    </v-container>
  </v-container>
</template>

<script>
import ResetPasswordForm from "../../components/auth/ResetPasswordForm.vue";
export default {
  name: "ResetPassword",
  components: { ResetPasswordForm },
};
</script>

<style scoped>
img {
  height: 300px;
  width: 300px;
}
</style>
