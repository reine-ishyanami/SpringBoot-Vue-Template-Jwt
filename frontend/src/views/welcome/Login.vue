<script setup>

import {reactive, ref} from "vue";
import {login} from '@/api'
import router from "@/router";

const visible = ref(false)

const form = reactive({
  username: '',
  password: '',
  remember: false
})

const emit = defineEmits(['alert'])

const rules = reactive({
  usernameRule: [
    v => !!v || "请输入用户名或邮箱",
    v => (v && v.length <= 20) || "用户名或邮箱必须小于20个字符"
  ],
  passwordRule: [
    v => !!v || "请输入密码",
    v => (v && v.length <= 20) || "密码必须小于10个字符",
    v => (v && v.length >= 6) || "密码必须大于6个字符"
  ]
})

const formRef = ref()

const alertType = reactive({
  type: 'success',
  message: '欢迎访问网站'
})

const loginAction = async () => {
  const {valid} = await formRef.value.validate()
  if (valid) {
    login(form.username, form.password, form.remember, (data) => {
      alertType.message = `登录成功，欢迎${data.username}进入系统`
      alertType.type = "success"
      router.push('/index')
      emit('alert', {...alertType})
    }, (message) => {
      alertType.message = message
      alertType.type = "warning"
      emit('alert', {...alertType})
    }, () => {
      alertType.message = "登录异常"
      alertType.type = "error"
      emit('alert', {...alertType})
    })
  }
}

</script>

<template>
  <div style="position: relative;">
    <div style="margin-top: 30px; text-align: center;">
      <div style="font-size: 30px;font-weight: bold">登录</div>
      <div style="font-size: 15px;color: gray">请输入用户名密码进行登录</div>
    </div>
    <div style="margin-top: 100px">
      <v-card
          class="mx-auto pa-12 pb-8"
          elevation="8"
          max-width="448"
          rounded="lg"
      >
        <v-form ref="formRef">
          <div class="text-subtitle-2 text-medium-emphasis">Account</div>

          <v-text-field
              density="compact"
              variant="outlined"
              v-model="form.username"
              :rules="rules.usernameRule"
              :counter="10"
              placeholder="用户名/邮箱"
              type="text"
              prepend-inner-icon="mdi-account"
          />

          <div class="text-subtitle-2 text-medium-emphasis d-flex align-center justify-space-between">
            Password

            <router-link
                class="text-caption text-decoration-none text-blue"
                to="#"
                target="_blank"
            >
              忘记密码?
            </router-link>
          </div>

          <v-text-field
              density="compact"
              variant="outlined"
              @click:append-inner="visible = !visible"
              v-model="form.password"
              :rules="rules.passwordRule"
              :counter="20"
              placeholder="密码"
              autocomplete="off"
              prepend-inner-icon="mdi-lock"
              :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
              :type="visible ? 'text' : 'password'"
          />
          <v-row>
            <v-col style="text-align: left;">
              <v-checkbox v-model="form.remember" label="记住我"></v-checkbox>
            </v-col>
          </v-row>
          <v-btn
              block
              color="blue"
              variant="tonal"
              @click="loginAction"
          >
            立即登录
          </v-btn>
          <v-card-text class="text-center text-blue"  style="font-size: 10px">
            莫得账号
          </v-card-text>
          <v-btn
              block
              color="#ff9966"
              variant="tonal"
              @click="$router.push('/register')"
          >
            立即注册
          </v-btn>

        </v-form>
      </v-card>
    </div>
    <div>

    </div>
  </div>
</template>

<style scoped>
</style>