<script setup>

import {reactive, ref} from "vue";
import {login} from '@/api'
import {storeAccessToken} from "@/api/token";

const visible = ref(false)

const form = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = reactive({
  usernameRule: [
    v => !!v || "请输入用户名或邮箱",
    v => (v && v.length <= 20) || "用户名或邮箱必须小于20个字符"
  ],
  passwordRule: [
    v => !!v || "请输入密码",
    v => (v && v.length <= 10) || "密码必须小于10个字符"
  ]
})

const formRef = ref()
const dialog = ref(false)
const message = ref("")

const loginAction = async () => {
  const {valid} = await formRef.value.validate()
  console.log(valid);
  if (valid) {
    login(form.username, form.password, form.remember, (data) => {
      storeAccessToken(data.token, form.remember, data.expire)
      message.value = "登录成功"
    }, () => {
      message.value = "登录失败"
    }, () => {
      message.value = "登录异常"
    })
    dialog.value = true
  }
}

const closeDialog = () => {
  dialog.value = false
  message.value = ""
}

</script>

<template>
  <v-dialog
      v-model="dialog"
      width="auto"
  >
    <v-card>
      <v-card-text>
        {{ message }}
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" block @click="closeDialog()">关闭弹窗</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <div style="margin: 0 20px">
    <div style="margin-top: 50px; text-align: center;">
      <div style="font-size: 30px;font-weight: bold">登录</div>
      <div style="font-size: 15px;color: gray">请输入用户名密码进行登录</div>
    </div>
    <div style="margin-top: 50px">
      <v-card
          class="mx-auto pa-12 pb-8"
          elevation="8"
          max-width="448"
          rounded="lg"
      >
        <v-form ref="formRef">
          <div class="text-subtitle-1 text-medium-emphasis">Account</div>

          <v-text-field
              density="compact"
              variant="outlined"
              v-model="form.username"
              :rules="rules.usernameRule"
              :counter="10"
              placeholder="用户名/邮箱"
              type="text"
              prepend-inner-icon="mdi-account"
          >
          </v-text-field>

          <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
            Password

            <router-link
                class="text-caption text-decoration-none text-blue"
                to="#"
                rel="noopener noreferrer"
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
              prepend-inner-icon="mdi-lock"
              :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
              :type="visible ? 'text' : 'password'"
          >
          </v-text-field>
          <v-row>
            <v-col style="text-align: left;">
              <v-checkbox v-model="form.remember" label="记住我"></v-checkbox>
            </v-col>
          </v-row>
          <v-btn
              block
              class="mb-8"
              color="blue"
              size="large"
              variant="tonal"
              @click="loginAction"
          >
            立即登录
          </v-btn>

          <v-card-text class="text-center">
            <router-link
                class="text-blue text-decoration-none"
                to=""
                rel="noopener noreferrer"
                target="_blank"
            >
              莫得账号，马上注册
              <v-icon icon="mdi-chevron-right"></v-icon>
            </router-link>
          </v-card-text>
        </v-form>
      </v-card>
    </div>
    <div>

    </div>
  </div>
</template>

<style scoped>
</style>