<template>
  <v-container>
    <v-row class="description" style="font-size: 50px;" justify="center"
      >Reset your password
    </v-row>
    <v-row class="description" style="font-size: 27.5px;" justify="center">
      Password should contain at least one special character, number and
      uppercase letter
    </v-row>
    <v-form v-model="valid" ref="form">
      <div class="example-custom-css">
        <div class="input-container">
          <v-text-field
            id="custom-css-input"
            filled
            v-model="password"
            ref="password"
            label="Password"
            style="font-size: 18px"
            :rules="[rules.required, rules.counter]"
            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :type="showPassword ? 'text' : 'password'"
            @click:append="showPassword = !showPassword"
            required
          />
          <password-meter :password="password" @score="onScore" />
        </div>
      </div>
      <v-text-field
        filled
        v-model="confirmPassword"
        label="Confirm password"
        style="font-size: 18px"
        :rules="[rules.required, rules.counter, rules.passwordMatch]"
        :append-icon="showConfirmPassword ? 'mdi-eye' : 'mdi-eye-off'"
        :type="showConfirmPassword ? 'text' : 'password'"
        @click:append="showConfirmPassword = !showConfirmPassword"
        required
      />
      <v-row>
        <v-btn
          @click="resetPassword()"
          color="rgb(104, 80, 215)"
          block
          class="description white--text"
          style="font-size: 15px"
        >
          <b>Reset password</b>
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
import passwordMeter from "vue-simple-password-meter";
const apiURL = "/auth/reset-password";

export default {
  name: "ResetPasswordForm",
  components: { passwordMeter },
  data() {
    return {
      password: "",
      confirmPassword: "",
      rules: {
        required: (value) => !!value || "Field is required.",
        counter: (value) =>
          value ? value.length > 9 : false || "Password must have a minimum of 10 characters",
        passwordMatch: () =>
          this.password == this.confirmPassword || "Passwords must match.",
      },
      valid: true,
      error: false,
      score: null,
      strength: null,
      showConfirmPassword: false,
      showPassword: false,
    };
  },
  methods: {
    resetPassword() {
      let key = this.$route.params.key;
      this.$refs.form.validate();
      if (!this.valid) return;
      if (this.password != this.confirmPassword) return;
      if (this.score <= 2) {
        alert("Your password is weak! Use numbers and special characters!");
        return;
      }
      let resetPassword = {
        newPassword: this.password,
        resetKey: key,
      };
      this.axios
        .post(apiURL, resetPassword)
        .then(() => {
          alert("You've successfully changed your password!");
          this.$refs.form.reset();
        })
        .catch(() => {
          alert(
            "Don't use your personal data as a password. Check if password contains at least one uppercase letter, one number and one special character. If you already used this link to reset your password, please request a new reset key."
          );
        });
    },
    onScore({ score, strength }) {
      // console.log(score); // from 0 to 4
      // console.log(strength); // one of : 'risky', 'guessable', 'weak', 'safe' , 'secure'
      this.score = score;
      this.strength = strength;
    },
  },
};
</script>

<style lang="scss">
.description {
  font-family: "Baloo2", Helvetica, Arial;
}

.row {
  display: flex;
  flex-wrap: wrap;
  flex: 1 1 auto;
  margin: 0px;
}

.wrap {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  max-width: 400px;
  margin: 40px auto;
}

.input {
  border: 1px solid #ccc;
  border-radius: 0.5rem;
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.example-custom-css {
  .po-password-strength-bar {
    border-radius: 2px;
    transition: all 0.2s linear;
    height: 30px;
    width: 30px;
    position: absolute;
    right: 50px;
    margin-top: auto;
    margin-bottom: 30px;
  }

  .po-password-strength-bar.risky {
    background-color: #f95e68;
  }

  .po-password-strength-bar.guessable {
    background-color: #fb964d;
  }

  .po-password-strength-bar.weak {
    background-color: #fdd244;
  }

  .po-password-strength-bar.safe {
    background-color: #b0dc53;
  }

  .po-password-strength-bar.secure {
    background-color: #35cc62;
  }
}
</style>
