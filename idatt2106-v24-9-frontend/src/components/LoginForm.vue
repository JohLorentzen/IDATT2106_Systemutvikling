<template>
  <div
    class="min-h-screen bg-background flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8"
  >
    <div class="max-w-md w-full space-y-8">
      <div>
        <ToastComponent :position="toastPosition" />
        <img class="mx-auto h-auto w-auto" src="@/assets/Logo-sparesti.png" :alt="text.altLogo" />
        <h2 class="text-center text-primary-text text-3xl font-extrabold">
          {{ text.login }}
        </h2>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="handleSubmit">
        <input type="hidden" name="remember" value="true" />
        <div class="rounded-md shadow-sm -space-y-px">
          <div class="mb-4">
            <label id="email-label" for="email-address" class="sr-only">
              {{ text.loginEmail }}</label
            >
            <input
              id="email-address"
              name="email"
              autocomplete="email"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
              :placeholder="text.loginEmail"
              v-model="formData.email"
            />
          </div>
          <div class="mb-4">
            <label id="password-label" for="password" class="sr-only">{{ text.password }}</label>
            <input
              id="password"
              name="password"
              type="password"
              autocomplete="current-password"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
              :placeholder="text.password"
              v-model="formData.password"
            />
          </div>
        </div>

        <div>
          <button
            type="submit"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-primaryBtn hover:bg-hooverBtn focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            {{ text.login }}
          </button>
        </div>
      </form>
      <div class="text-sm text-center">
        <a
          href="/registration"
          id="register-link"
          class="font-medium text-indigo-600 hover:text-indigo-500"
        >
          {{ text.register }}
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import text from '@/assets/text.no.json'
import router from '@/router'
import type { AxiosResponse } from 'axios'
import { AxiosError } from 'axios'
import type { LoginResponse, User, UserInsight } from '@/services/userService/types'
import api from '@/services/userService/'

import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { useToast } from 'primevue/usetoast'
const toast = useToast()
const screenWidth = ref(window.innerWidth)
const toastPosition = computed(() => {
  return screenWidth.value < 768 ? 'top-center' : 'bottom-right'
})

const userStore = useUserStore()

const formData: User = {
  email: '',
  password: ''
}

/**
 * Handles the form submit event.
 *
 * Sets valid tokens in session storage and redirects user to the correct page.
 * If an error occurs, a toast message is shown, with the correct error message
 */
const handleSubmit = async () => {
  try {
    const response: AxiosResponse<LoginResponse> = await api.loginUser(formData)

    const { token, refreshToken }: LoginResponse = response.data
    const fullName = response.data.fullName

    sessionStorage.setItem('accessToken', token)
    sessionStorage.setItem('refreshToken', refreshToken)

    if (response && response.status === 200) {
      userStore.saveUserInStore({
        fullName: fullName,
        email: formData.email,
        password: ''
      })

      const response: AxiosResponse<UserInsight> = await api.getUserInsight()
      if (response.status === 204) {
        userStore.hasUserInsight = false
        router.push('/userSurvey')
      } else {
        router.push('/')
      }
    }
  } catch (error: unknown) {
    if (error instanceof AxiosError && error.response) {
      if (error.response.status === 500) {
        toast.add({
          severity: 'error',
          summary: 'Innlogging mislykket',
          detail: 'En tjenerfeil oppstod. Vennligst forsøk på nytt!',
          life: 5000
        })
      }
      if (error.response.status === 401) {
        toast.add({
          severity: 'error',
          summary: 'Innlogging mislykket',
          detail: 'Feil brukernavn/passord.',
          life: 5000
        })
      }
      if (error.response.status === 404) {
        toast.add({
          severity: 'error',
          summary: 'Innlogging mislykket',
          detail: 'Brukernavnet eksisterer ikke.',
          life: 5000
        })
      }
    } else {
      toast.add({
        severity: 'error',
        summary: 'Ukjent feil',
        detail: 'En ukjent feil oppstod. Vennligst forsøk på nytt!',
        life: 5000
      })
    }
  }
}
</script>
