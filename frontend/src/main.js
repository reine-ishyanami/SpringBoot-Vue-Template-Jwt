import {createApp} from 'vue'
import App from './App.vue'
import router from "@/router";
import '@mdi/font/css/materialdesignicons.css'

// Vuetify
import 'vuetify/styles'
import {createVuetify} from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import axios from "axios";

axios.defaults.baseURL = 'http://127.0.0.1:8080'

const vuetify = createVuetify({
    icons: {
        defaultSet: 'mdi'
    },
    components,
    directives
})

createApp(App)
    .use(router)
    .use(vuetify)
    .mount('#app')
