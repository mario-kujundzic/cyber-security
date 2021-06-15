<template>
  <v-container>
    <v-row class="description" style="font-size: 70px;" justify="center"
      >Trouble logging in?</v-row
    >
    <v-row class="description" style="font-size: 33px;" justify="center"
      >Enter your email and we'll send you a link to get back into your account
    </v-row>
    <v-form v-model="valid" ref="form">
      <v-row justify="center">
        <v-text-field
          filled
          v-model="username"
          :rules="[rules.email, rules.required]"
          label="Username"
          type="text"
          style="font-size: 18px"
        />
      </v-row>
      <v-row>
        <v-btn
          @click="forgotPassword()"
          :disabled="!valid"
          :loading = 'loading'
          color="#8C9EFF"
          block
          class="description"
          style="font-size: 15px"
        >
          <b>Confirm email</b>
        </v-btn>
      </v-row>
    </v-form>
    <v-row justify="center" class="pt-5">
      <router-link :to="{ name: 'Login' }" style="text-decoration: none;"
        ><p class="description" style="font-size: 20px">
          Back to Login
        </p></router-link
      >
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "/auth/forgot-password";

export default {
  name: "ForgotPasswordForm",
  data() {
    return {
      username: "",
      rules: {
        required: (value) => !!value || "Email is required.",
        email: (value) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        },
      },
      valid: true,
      error: false,
      loading: false
    };
  },
  methods: {
    forgotPassword: function() {
      this.loading = true;
      this.$refs.form.validate();
      if (!this.valid) {
        this.loading = false;
        return;
      }
      var config = {
        headers: {
          "Content-Type": "text/plain",
        },
      };
      this.axios
        .post(apiURL, this.username, config)
        .then(() => {
          this.loading = false;
          alert("We've sent you recovery link! Please check your email!");
          this.$refs.form.reset();
        })
        .catch((error) => {
          this.loading = false;
          alert(error.response.data.message);
        });
    },
  },
};
</script>

<style>
.description {
  font-family: "Baloo2", Helvetica, Arial;
}

.row {
  display: flex;
  flex-wrap: wrap;
  flex: 1 1 auto;
  margin: 0px;
}
</style>
