<template>
<v-container fill-height>
    <v-container>
        <v-row align="center" justify="center">
            <h1>Log in to your account!</h1>
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
                    <v-row>
                        <v-col class="mt-6">
                            <v-btn color="#c2e5cf" @click="login()" block>Login</v-btn>
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
                        role : response.data.role,
                        username : response.data.username,
                        name : response.data.name,
                        surname: response.data.surname
                    }
                    console.log(loggedInUser);
                    localStorage.setItem('role', response.data.role);
                    localStorage.setItem('authKey', 'Bearer ' + response.data.accessToken);
                    localStorage.setItem('user', JSON.stringify(loggedInUser));
                    this.axios.defaults.headers['Authorization'] = 'Bearer ' + response.data.accessToken;
                    this.$router.push({ name: "ViewCertificates" })
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
