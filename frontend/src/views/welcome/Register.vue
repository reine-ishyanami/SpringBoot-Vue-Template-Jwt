<script setup>

import {computed, onMounted, reactive, ref} from "vue";
import {askRegisterCode, register} from "@/api";
import router from "@/router";

const formRef = ref()
const visible = ref(false)
const visible_repeat = ref(false)
const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})
const rules = reactive({
  usernameRule: [
    v => !!v || '请输入用户名',
    v => (v && v.length <= 10) || '用户名必须小于10个字符',
    v => (v && /^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(v)) || '用户名不能包含特殊字符，只能是汉字字母与数字的组合'
  ],
  emailRule: [
    v => !!v || '请输入电子邮箱地址',
    v => (v && isValidEmail) || '邮箱地址不合法，请重新输入'
  ],
  passwordRule: [
    v => !!v || '请输入密码',
    v => (v && v.length <= 20) || '密码必须小于10个字符',
    v => (v && v.length >= 6) || '密码必须大于6个字符'
  ],
  passwordRepeatRule: [
    v => !!v || '请再次输入密码',
    v => (v && v === form.password) || '两次输入的密码不一致'
  ],
  codeRule: [
    v => !!v || '请输入验证码',
    v => (v && v.length === 6) || '验证码长度有误'
  ]
})
const alertType = reactive({
  type: 'success',
  message: '欢迎访问网站'
})
const emit = defineEmits(['alert'])
const cool = ref(0)

const isValidEmail = computed(() => /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(form.email))

const getCodeAction = () => {
  if (isValidEmail) {
    cool.value = 60
    askRegisterCode(form.email, () => {
      alertType.message = '验证码已发送到您的邮箱，请注意查收'
      alertType.type = 'success'
      emit('alert', alertType)
      const coolTimer = setInterval(() => {
        cool.value--
        if (cool.value === 0) {
          clearInterval(coolTimer)
        }
      }, 1000)
    }, (message) => {
      cool.value = 0
      alertType.message = message
      alertType.type = 'warning'
      emit('alert', alertType)
    })
  }
}

const registerAction = async () => {
  const {valid} = await formRef.value.validate()
  if (valid) {
    register(form, () => {
      alertType.message = '用户注册成功'
      alertType.type = 'success'
      router.push('/')
      emit('alert', alertType)
    }, (message) => {
      alertType.message = message
      alertType.type = 'warning'
      emit('alert', alertType)
    })
  }
}


</script>

<template>
  <div style="position: relative;">
    <div style="margin-top: 30px; text-align: center;">
      <div style="font-size: 30px;font-weight: bold">注册</div>
      <div style="font-size: 15px;color: gray">欢迎注册新用户</div>
    </div>
    <div style="margin-top: 20px">
      <v-card
          class="mx-auto pa-12 pb-8"
          elevation="8"
          max-width="448"
          rounded="lg"
          style="font-size: 5px;"
      >
        <v-form ref="formRef">
          <div class="text-subtitle-2 text-medium-emphasis">Username</div>

          <v-text-field
              density="compact"
              variant="outlined"
              v-model="form.username"
              :rules="rules.usernameRule"
              :counter="10"
              placeholder="用户名"
              type="text"
              prepend-inner-icon="mdi-account"
          />

          <div class="text-subtitle-2 text-medium-emphasis">Password</div>

          <v-text-field
              density="compact"
              variant="outlined"
              @click:append-inner="visible = !visible"
              v-model="form.password"
              :rules="rules.passwordRule"
              :counter="20"
              autocomplete="off"
              placeholder="密码"
              prepend-inner-icon="mdi-lock"
              :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
              :type="visible ? 'text' : 'password'"
          />
          <div class="text-subtitle-2 text-medium-emphasis">Password Repeat</div>

          <v-text-field
              density="compact"
              variant="outlined"
              @click:append-inner="visible_repeat = !visible_repeat"
              v-model="form.password_repeat"
              :rules="rules.passwordRepeatRule"
              :counter="20"
              autocomplete="off"
              placeholder="确认密码"
              prepend-inner-icon="mdi-lock"
              :append-inner-icon="visible_repeat ? 'mdi-eye-off' : 'mdi-eye'"
              :type="visible_repeat ? 'text' : 'password'"
          />

          <div class="text-subtitle-2 text-medium-emphasis">Email</div>

          <v-text-field
              density="compact"
              variant="outlined"
              v-model="form.email"
              :rules="rules.emailRule"
              placeholder="邮箱"
              type="text"
              prepend-inner-icon="mdi-email"
          />

          <div class="text-subtitle-2 text-medium-emphasis">Verify Code</div>

          <v-text-field
              density="compact"
              variant="outlined"
              v-model="form.code"
              :rules="rules.codeRule"
              placeholder="验证码"
              type="text"
              prepend-inner-icon="mdi-pen"
          >
            <template #append-inner>
              <v-btn
                  :disabled="!isValidEmail || cool!==0"
                  size="small"
                  @click="getCodeAction"
                  width="100"
              >
                {{ cool > 0 ? cool : '获取验证码' }}
              </v-btn>
            </template>
          </v-text-field>

          <v-btn
              block
              color="#ff6666"
              variant="tonal"
              @click="registerAction"
          >
            立即注册
          </v-btn>
          <v-card-text class="text-center text-blue" style="font-size: 10px" @click="$router.push('/')">
            <a
                class="text-blue text-decoration-none"
                href="#"
                rel="noopener noreferrer"
            >
              已有账号，返回登录
              <v-icon icon="mdi-chevron-right"></v-icon>
            </a>
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