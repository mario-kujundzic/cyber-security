<template>
  <v-content>
    <v-container>
        <v-row align="center" justify="center" class="mt-10">
            <h1>Login to your account!</h1>
        </v-row>
        <v-row align="center" justify="center">
            <v-col>
                    <v-form v-model="valid" ref="form">
                        <v-row class="pt-10">
                            <v-text-field
                                v-model="user.username"
                                :rules="[rules.email, rules.required]"
                                label="Username"
                                prepend-icon="mdi-account"
                                type="text" 
                            />
                        </v-row>
                        <v-row>
                        <v-text-field
                            v-model="user.password"
                            :rules="[rules.required]"
                            label="Password"
                            prepend-icon="mdi-lock"
                            type="password" 
                        />
                        </v-row>
                    </v-form>
                    <v-row align="center" justify="center">
                        <a>FORGOT PASSWORD?</a>
                    </v-row>
                    <v-row class="pt-15">
                        <v-col>
                            <v-btn color="#8C9EFF" @click="login()" block>Login</v-btn>
                        </v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <a>NOT REGISTERED YET? SIGN-UP!</a>
                    </v-row>
            </v-col>
        </v-row>
    </v-container>
  </v-content>
</template>
<script>
const apiURL = "/auth/login";

export default {
    name: 'Login',
    data() {
        return {
            user: {
                username: '',
                password: ''
            },
            rules: {
                required: value => !!value || 'Field is required.',
                email: value => {
                    const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                    return pattern.test(value) || 'Invalid e-mail.' 
                }
            },
            valid: true,
            error: false
        }
    },
    methods: {
        login: function() {
            this.$refs.form.validate();
            if (!this.valid)
                return;
            this.axios({
                url: apiURL,
                method: 'POST',
                data: this.user
            }).then(
                response => {
                    let loggedInUser = {
                        id : response.data.id,
                        token : response.data.accessToken,
                        expiresIn : response.data.expiresIn,
                        role : response.data.userRole
                    }
                    //localStorage.setItem('role', response.data.userRole);
                    //localStorage.setItem('authKey', 'Bearer ' + response.data.accessToken);
                    localStorage.setItem('user', JSON.stringify(loggedInUser));
                    //this.$user.set(Object.assign(response, { role: response.data.userRole, auth: 'Bearer ' + response.data.accessToken}))
                    //this.axios.defaults.headers['Authorization'] = 'Bearer ' + response.data.accessToken;
                    alert('Logged in ' + localStorage.getItem('user'))
                    //alert('Logged in: ' + this.$user['role'] + this.$user['key']);
                    this.$router.push({ name: "Home" })
                }
                
            )
        }
    }
}
</script>

<style scoped>
    a {
        font-size: small;
    }
</style>
