<script setup>

import {reactive} from "vue";

const alertArgs = reactive({
  alert: false,
  type: 'success',
  message: ''
})

const alertAction = ({type, message}) => {
  alertArgs.type = type
  alertArgs.message = message
  alertArgs.alert = true
  const timer = setInterval(() => {
    alertArgs.alert = false
    clearInterval(timer)
  }, 3000)
}


</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1;background-color: black">
      <v-img
          width="100%"
          height="100%"
          cover
          aspect-ratio="16/9"
          src="https://img1.baidu.com/it/u=4097856652,4033702227&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800"
      ></v-img>
    </div>
    <div class="welcome-text">
      <div>Vue3 登录界面模板</div>
    </div>
    <div class="right-card">
      <div style="position: absolute;">
        <v-alert
            v-model="alertArgs.alert"
            closable
            :type="alertArgs.type"
            width="500"
        >
          {{ alertArgs.message }}
        </v-alert>
      </div>

      <v-fade-transition>
        <router-view @alert="alertAction"/>
      </v-fade-transition>
    </div>
  </div>
</template>

<style scoped>
.welcome-text {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}

.right-card {
  width: 500px;
  z-index: 1;
  background-color: azure;
}
</style>