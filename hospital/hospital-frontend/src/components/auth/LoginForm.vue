<template>
  <v-container fill-height>
    <v-container>
      <v-row
        align="center"
        justify="center"
        class="description"
        style="font-size: 50px"
      >
        <b>Login Account</b>
      </v-row>
      <v-row
        align="center"
        justify="center"
        class="description"
        style="font-size: 30px"
      >
        Nice to see you again!
      </v-row>
      <v-row align="center" justify="center">
        <v-col>
          <v-form v-model="valid" ref="form">
            <v-row>
              <v-text-field
                v-model="user.username"
                :rules="[rules.email, rules.required]"
                label="Username"
                prepend-icon="mdi-account"
                type="text"
                class="description"
                style="font-size: 18px"
              />
            </v-row>
            <v-row>
              <v-text-field
                v-model="user.password"
                :rules="[rules.required]"
                label="Password"
                prepend-icon="mdi-lock"
                class="description"
                style="font-size: 18px"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                :type="showPassword ? 'text' : 'password'"
                @click:append="showPassword = !showPassword"
              />
            </v-row>
          </v-form>
          <v-row align="center" justify="center">
            <router-link
              :to="{ name: 'ForgotPassword' }"
              style="text-decoration: none;"
              ><p class="description" style="font-size: 20px">
                Forgot password?
              </p></router-link
            >
          </v-row>
          <v-row>
            <v-col class="mt-6">
              <v-btn
                color="#8C9EFF"
                @click="login()"
                block
                class="description"
                style="font-size: 15px"
                :loading="loading"
                ><b>Login</b></v-btn
              >
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>
<script>
const apiURL = "/auth/login";

export default {
  name: "Login",
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      rules: {
        required: (value) => !!value || "Field is required.",
        email: (value) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        },
      },
      valid: true,
      error: false,
      showPassword: false,
      loading: false
    };
  },
  methods: {
    login: function() {
      this.loading = true;
      this.$refs.form.validate();
      if (!this.valid) {
        this.loading = false;
        return;
      }
      this.axios({
        url: apiURL,
        method: "POST",
        data: this.user,
      })
        .then((response) => {
          let loggedInUser = {
            id: response.data.id,
            token: response.data.accessToken,
            expiresIn: response.data.expiresIn,
            role: response.data.role,
            username: response.data.username,
            name: response.data.name,
            surname: response.data.surname,
          };
          localStorage.setItem("role", response.data.role);
          localStorage.setItem(
            "authKey",
            "Bearer " + response.data.accessToken
          );
          localStorage.setItem("user", JSON.stringify(loggedInUser));
          this.axios.defaults.headers["Authorization"] =
            "Bearer " + response.data.accessToken;
          this.loading = false;
          if (loggedInUser.role === "ADMIN") {
            this.$router.push({ name: "CSRForm" });
          }
          else {
            this.$router.push({ name: "ViewPatients" });
          }
        })
        .catch((error) => {
          //TODO sredi ovo na snackbarove u nekom trenutku
          this.loading = false;
          alert(error.response.data.message);
        });
    },
  },
};
</script>

<style scoped>
.description {
  font-family: "Baloo2", Helvetica, Arial;
}
</style>
