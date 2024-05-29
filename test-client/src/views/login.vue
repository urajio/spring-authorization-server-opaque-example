<template>
    <h1>
        LOGIN PAGE
    </h1>
    <div>
        <button @click="login">
            LOGIN (authorization code)
        </button>
    </div>
    <div style="margin-top: 20px">
        <button @click="loginClient">
            LOGIN (client credentials)
        </button>
    </div>
</template>

<script>
    import LoginService from "@/services/login-service";

    export default {
        name: "login",
        methods: {
            login() {
                return LoginService.login();
            },
            loginClient() {
                return LoginService.loginClient().then(() => {
                    this.$router.replace({name: "home"});
                })
            }
        }
    }
</script>

<style scoped>

</style>