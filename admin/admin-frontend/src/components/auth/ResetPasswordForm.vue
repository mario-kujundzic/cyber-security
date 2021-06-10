<template>
  <v-container>
    <v-row class="description" style="font-size: 50px;" justify="center"
      >Reset your password
    </v-row>
    <v-form v-model="valid" ref="form">
      <div class="example-custom-css">
        <div class="input-container">
          <v-text-field
            id="custom-css-input"
            filled
            type="password"
            v-model="password"
            ref="password"
            label="Password"
            style="font-size: 18px"
            :rules="[rules.required, rules.counter]"
          />
          <password-meter :password="password" @score="onScore" />
        </div>
      </div>
      <v-text-field
        filled
        v-model="confirmPassword"
        type="password"
        label="Confirm password"
        style="font-size: 18px"
        :rules="[rules.required, rules.counter, rules.passwordMatch]"
      />
      <v-row>
        <v-btn
          @click="forgotPassword()"
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
const apiURL = "/auth/forgot-password";

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
          value.length > 4 || "Password must have a minimum of 5 characters",
        passwordMatch: () =>
          this.password == this.confirmPassword || "Passwords must match.",
      },
      valid: true,
      error: false,
      score: null,
    };
  },
  methods: {
    forgotPassword() {
      this.$refs.form.validate();
      if (!this.valid) return;
      var config = {
        headers: {
          "Content-Type": "text/plain",
        },
      };
      this.axios
        .post(apiURL, this.username, config)
        .then(() => {
          alert("We've sent you recovery link! Please check your email!");
          this.$refs.form.reset();
        })
        .catch(() => {
          alert("Please check your email!");
        });
    },
    onScore({ score, strength }) {
      console.log(score); // from 0 to 4
      console.log(strength); // one of : 'risky', 'guessable', 'weak', 'safe' , 'secure'
      this.score = score;
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
    right: 10px;
    margin-top: auto;
    margin-bottom: 25px;
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
