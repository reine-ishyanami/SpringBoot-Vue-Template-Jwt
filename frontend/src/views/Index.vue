<script setup>
import {logout} from "@/api";
import router from "@/router";

const userLogout = () => {
  logout(() => {
    router.push('/')
  })
}
</script>

<template>
  <div>
    <v-btn @click="userLogout">退出登录</v-btn>
  </div>
</template>

<style scoped>

</style>