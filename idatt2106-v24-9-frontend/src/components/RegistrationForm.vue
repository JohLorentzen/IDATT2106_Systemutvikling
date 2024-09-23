<template>
  <div
    class="min-h-screen bg-background bg-cover bg-no-repeat bg-center flex flex-col items-center justify-start px-6 text-primary-text"
  >
    <div class="w-full max-w-xs">
      <ToastComponent :position="toastPosition" />
      <form
        class="shadow-md rounded px-8 pt-6 pb-8 mb-4 bg-white mt-20"
        @submit.prevent="handleSubmit"
      >
        <div class="mb-4">
          <img class="mx-auto h-12 w-auto" src="@/assets/logo.svg" alt="Piggy bank icon" />
        </div>
        <h2 class="text-center text-2xl font-extrabold text-primary-text">{{ text.welcome }}</h2>
        <p v-if="emptyFieldsError" class="text-red-500 text-xs italic">{{ emptyFieldsError }}</p>
        <div class="mb-4">
          <label id="name-label" class="block text-gray-700 text-sm font-bold mb-2" for="name">
            {{ text.name }}
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="name"
            type="text"
            placeholder="Ola Nordmann"
            v-model="user.fullName"
          />
        </div>
        <div class="mb-4">
          <label id="email-label" class="block text-gray-700 text-sm font-bold mb-2" for="email">
            {{ text.email }}
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="email"
            type="email"
            placeholder="ola@example.com"
            v-model="user.email"
          />
          <p v-if="emailError" class="text-red-500 text-xs italic">{{ emailError }}</p>
        </div>
        <div class="mb-4">
          <label id="mobile-label" class="block text-gray-700 text-sm font-bold mb-2" for="mobile">
            {{ text.phoneNumber }}
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="mobile"
            type="tel"
            placeholder="12 34 56 78"
            v-model="user.phoneNumber"
          />
        </div>
        <div class="mb-4">
          <label
            id="password-label"
            class="block text-gray-700 text-sm font-bold mb-2"
            for="password"
          >
            {{ text.password }}
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
            id="password"
            type="password"
            :placeholder="text.password"
            v-model="user.password"
          />
          <p v-if="passwordError" class="text-red-500 text-xs italic">{{ passwordError }}</p>
        </div>
        <div class="mb-6">
          <label
            id="confirm-password-label"
            class="block text-gray-700 text-sm font-bold mb-2"
            for="confirm-password"
          >
            {{ text.ConfirmPassword }}
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="confirm-password"
            type="password"
            :placeholder="text.ConfirmPassword"
            v-model="confirmedPassword"
          />
          <p v-if="confirmPasswordError" class="text-primaryBtn text-xs italic">
            {{ confirmPasswordError }}
          </p>
        </div>
        <div class="place-self-center">
          <button
            class="bg-primaryBtn w-full hover:bg-hooverBtn text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            type="submit"
          >
            {{ text.nextBtn }}
          </button>
        </div>
      </form>
      <div class="text-sm text-center">
        <a href="/login" class="font-medium text-indigo-600 hover:text-indigo-500">
          {{ text.reglogin }}
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import text from '@/assets/text.no.json'
import router from '@/router'
import type { User } from '@/services/userService/types'
import { AxiosError } from 'axios'
import api from '@/services/userService/'

import { useToast } from 'primevue/usetoast'
const toast = useToast()
const screenWidth = ref(window.innerWidth)
const toastPosition = computed(() => {
  return screenWidth.value < 768 ? 'top-center' : 'bottom-right'
})

const user = ref<User>({
  email: '',
  password: '',
  fullName: '',
  phoneNumber: '',
  createdAt: new Date()
})

const emit = defineEmits(['submit'])

const confirmedPassword = ref('')
const emailError = ref('')
const passwordError = ref('')
const confirmPasswordError = ref('')
const emptyFieldsError = ref('')

/**
 * Validates the email address.
 * @param email a string representing the email address.
 */
function validateEmail(email: string) {
  const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  return regex.test(email)
}

/**
 * Checks if the password meets the security requirements.
 * Requires at least 8 characters, one uppercase letter, one number and one special character.
 *
 * @param password a string representing the password.
 */
function checkPasswordSecurity(password: string): boolean {
  const hasUpperCase = /[A-Z]/.test(password)
  const hasNumber = /\d/.test(password)
  const hasSpecialChar = /[^A-Za-z0-9]/.test(password)
  return password.length >= 8 && hasUpperCase && hasNumber && hasSpecialChar
}

/**
 * Checks if the form is valid and submits the form.
 * Handles errors accordingly.
 */
async function handleSubmit() {
  let isValid = true
  emailError.value = ''
  passwordError.value = ''
  confirmPasswordError.value = ''
  emptyFieldsError.value = ''

  if (!user.value.email || !user.value.fullName || !user.value.phoneNumber) {
    emptyFieldsError.value = text.emptyFields
    isValid = false
  }

  if (!validateEmail(user.value.email)) {
    emailError.value = text.invalidEmail
    isValid = false
  }

  if (!checkPasswordSecurity(user.value.password)) {
    passwordError.value = text.securityRequirements

    isValid = false
  }

  if (user.value.password !== confirmedPassword.value) {
    confirmPasswordError.value = text.passwordsDoNotMatch
    isValid = false
  }

  if (isValid) {
    emit('submit', user.value)
    try {
      const response = await api.registerUser(user.value)

      if (response.status === 200) {
        toast.add({
          severity: 'success',
          summary: 'Registrering vellykket',
          life: 1000
        })
        setTimeout(() => {
          router.push('/login')
        }, 500)
      }
    } catch (error: unknown) {
      if (error instanceof AxiosError && error.response) {
        if (error.response.status === 500) {
          toast.add({
            severity: 'error',
            summary: 'Registrering mislykket',
            detail:
              'En tjenerfeil oppstod under registreringen av brukeren din. Vennligst forsøk på nytt!',
            life: 5000
          })
        } else if (error.response.status === 409) {
          toast.add({
            severity: 'error',
            summary: 'Registrering mislykket',
            detail: 'Brukernavn/e-postadresse eksisterer fra før av. Vennligst velg et annet!',
            life: 5000
          })
        }
      } else {
        toast.add({
          severity: 'error',
          summary: 'Ukjent feil',
          detail:
            'En ukjent feil oppstod under registreringen av brukeren din. Vennligst forsøk på nytt!',
          life: 5000
        })
      }
    }
  }
}
</script>
