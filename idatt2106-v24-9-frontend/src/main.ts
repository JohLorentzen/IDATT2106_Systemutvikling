import './assets/main.css'
import '@fortawesome/fontawesome-free'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
/* Toast imports */
import 'primevue/resources/themes/aura-light-green/theme.css'
import PrimeVue from 'primevue/config'
import Toast from 'primevue/toast'
import ToastService from 'primevue/toastservice'
import 'primeicons/primeicons.css'
import Button from 'primevue/button'
import ButtonGroup from 'primevue/buttongroup'

/* font awesome setup for app */
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { fas } from '@fortawesome/free-solid-svg-icons'

library.add(fas)

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(PrimeVue)
app.use(ToastService)
app.component('font-awesome-icon', FontAwesomeIcon)
app.component('ToastComponent', Toast)
app.component('ButtonBtn', Button)
app.component('ButtonGroup', ButtonGroup)

app.mount('#app')
