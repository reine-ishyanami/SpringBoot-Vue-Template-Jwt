<script setup>
import {VStepper} from "vuetify/labs/components";
import {computed, reactive, ref} from "vue";
import {askResetCode, resetConfirm, resetPassword} from "@/api";
import router from "@/router";

const visible = ref(false)
const visible_repeat = ref(false)
const active = ref(1)
const cool = ref(0)
const isValidEmail = computed(() => /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(form.email))
const form = reactive({
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})
const rules = reactive({
  emailRule: [
    v => !!v || '请输入电子邮箱地址',
    v => (v && isValidEmail) || '邮箱地址不合法，请重新输入'
  ],
  passwordRule: [
    v => !!v || '请输入新密码',
    v => (v && v.length <= 20) || '密码必须小于10个字符',
    v => (v && v.length >= 6) || '密码必须大于6个字符'
  ],
  passwordRepeatRule: [
    v => !!v || '请再次输入新密码',
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

const getCodeAction = () => {
  if (isValidEmail) {
    cool.value = 60
    askResetCode(form.email, () => {
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

const confirmCodeAction = () => {
  if (isValidEmail) {
    resetConfirm(form, () => {
      alertType.message = '验证码验证成功，请输入新密码'
      alertType.type = 'success'
      emit('alert', {...alertType})
      active.value++
    }, (message) => {
      alertType.message = message
      alertType.type = 'warning'
      emit('alert', {...alertType})
    })
  }
}

const resetPasswordAction = () => {
  if (form.password === form.password_repeat) {
    resetPassword(form, () => {
      alertType.message = '密码重置成功，请重新登录'
      alertType.type = 'success'
      emit('alert', {...alertType})
      router.push('/')
    }, (message) => {
      alertType.message = message
      alertType.type = 'warning'
      emit('alert', {...alertType})
    })
  }else {
    alertType.message = '两次输入的密码不一致'
    alertType.type = 'warning'
    emit('alert', {...alertType})
  }
}

</script>

<template>
  <div style="margin-top: 30px">
    <v-stepper
        alt-labels
        hide-actions
        :items="['验证电子邮件', '重新设定密码']"
        :model-value="active"
    >
      <template v-slot:item.1>
        <div style="margin-top: 30px; text-align: center;">
          <div style="font-size: 30px;font-weight: bold">重置密码</div>
          <div style="font-size: 15px;color: gray">请验证您的电子邮箱</div>
        </div>
        <div style="margin-top: 70px">
          <v-card
              class="mx-auto pa-12 pb-8"
              elevation="8"
              rounded="lg"
              style="font-size: 5px;"
          >

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
                color="#993333"
                variant="tonal"
                @click="confirmCodeAction"
            >
              立即验证
            </v-btn>
          </v-card>
        </div>
      </template>

      <template v-slot:item.2>
        <div style="margin-top: 30px; text-align: center;">
          <div style="font-size: 30px;font-weight: bold">重置密码</div>
          <div style="font-size: 15px;color: gray">请输入新密码</div>
        </div>
        <div style="margin-top: 70px">
          <v-card
              class="mx-auto pa-12 pb-8"
              elevation="8"
              rounded="lg"
              style="font-size: 5px;"
          >

            <div class="text-subtitle-2 text-medium-emphasis">Password</div>

            <v-text-field
                density="compact"
                variant="outlined"
                @click:append-inner="visible = !visible"
                v-model="form.password"
                :rules="rules.passwordRule"
                :counter="20"
                autocomplete="off"
                placeholder="新密码"
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
                placeholder="确认新密码"
                prepend-inner-icon="mdi-lock"
                :append-inner-icon="visible_repeat ? 'mdi-eye-off' : 'mdi-eye'"
                :type="visible_repeat ? 'text' : 'password'"
            />

            <v-btn
                block
                color="#663366"
                variant="tonal"
                @click="resetPasswordAction"
            >
              立即重置密码
            </v-btn>

          </v-card>
        </div>
      </template>
    </v-stepper>
  </div>
</template>

<style scoped>

</style>