<template>
  <div
    class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-75"
    @click.self="closeModal"
    v-if="challenge"
  >
    <div class="bg-white rounded-lg p-6 w-96 h-120 relative shadow-xl animate-fade-in-down">
      <div class="flex justify-center items-start mb-4">
        <h2 class="text-xl text-center text-primary-text">{{ text.receivedChallenge }}</h2>
        <button
          @click="closeModal"
          class="text-gray-500 hover:text-gray-700 focus:outline-none p-1.5"
        >
          <svg
            class="h-6 w-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            ></path>
          </svg>
        </button>
      </div>
      <div class="flex flex-col items-center mt-2 text-center gap-2">
        <p class="font-bold text-lg">{{ challenge.name }}</p>
        <p class="text-gray-600">
          {{ text.duration }} <strong>{{ challenge.duration }}</strong> {{ text.days }}
        </p>
        <div class="mt-4 p-3 bg-gray-100 rounded-lg flex items-center">
          <font-awesome-icon :icon="['fas', 'lightbulb']" style="color: #d4af37; font-size: 24px" />
          <p class="text-gray-800 font-medium ml-2">Tips: {{ challenge.tip }}</p>
        </div>
        <label class="font-medium mt-5">{{ text.howMuch }}</label>
        <div class="relative flex items-center justify-center mt-4">
          <input
            type="text"
            class="text-center text-2xl border-2 border-gray-500 focus:border-blue-500 rounded-lg"
            v-model="goalAmount"
            @input="handleInput"
          />
          <span class="text-2xl ml-2 text-gray-800">{{ text.currencySymbol }}</span>
        </div>
      </div>
      <div class="flex justify-center space-x-4 mt-auto pt-4">
        <button
          @click="acceptChallenge"
          class="bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded-lg transition duration-300"
        >
          {{ text.accept }}
        </button>
        <button
          @click="declineChallenge"
          class="sm:w-auto bg-white text-primary-text hover:ring-2 hover:ring-green-700 hover:shadow-lg rounded-md px-4 py-2 shadow-md border border-green-600 transition-all duration-200"
        >
          <font-awesome-icon :icon="['fas', 'arrows-rotate']" class="mr-2" />{{
            text.generateNewChallengeBtnText
          }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useUserChallengesStore } from '@/stores/userChallengeStore'
import type { Challenge } from '@/services/savingPath/types'
import text from '@/assets/text.no.json'
import api from '@/services/savingPath'
import challengeApi from '@/services/savingPath'
import { AxiosError } from 'axios'

const userChallengesStore = useUserChallengesStore()

const goalAmount = ref<number>(0)

const challenge = ref<Challenge | null>(null)

const emits = defineEmits(['close', 'challengeAccepted'])

onMounted(async () => {
  await challengeApi
    .getChallenge()
    .then((response) => {
      challenge.value = response.data
      goalAmount.value = challenge.value.sum
    })
    .catch((error) => {
      console.error('An unexpected error occurred:', error)
    })
})

/**
 * Fetches the next challenge from the API.
 */
async function nextChallenge() {
  await challengeApi
    .getChallenge()
    .then((response) => {
      challenge.value = response.data
      goalAmount.value = challenge.value.sum
    })
    .catch((error) => {
      console.error('An unexpected error occurred:', error)
    })
}

/**
 * Accepts the current challenge displayed in the modal.
 */
async function acceptChallenge() {
  const currentChallenge = challenge.value
  if (!currentChallenge) return

  try {
    await userChallengesStore.acceptChallenge({
      goalAmount: goalAmount.value,
      savedAmount: 0,
      fromDate: new Date(Date.now()),
      toDate: new Date(Date.now() + currentChallenge.duration * 24 * 60 * 60 * 1000),
      challenge: currentChallenge
    })
    closeModal()
    emits('challengeAccepted')
  } catch (error) {
    if (error instanceof AxiosError && error.response) {
      if (error.response.status === 400) {
        console.error('User login failed! Please try again later.')
      }
      if (error.response.status === 401) {
        console.error('Invalid credentials!')
      }
      if (error.response.status === 404) {
        console.error('Username/email not found!')
      }
    } else {
      console.error('An unexpected error occurred:', error)
    }
  }
}

/**
 * Declines the current challenge displayed in the modal.
 * Fetches a new challenge from the API.
 */
async function declineChallenge() {
  const currentChallenge = challenge.value
  if (!currentChallenge) return
  await api
    .postRejectedChallenge(currentChallenge)
    .then(() => {
      nextChallenge()
    })
    .catch((error) => {
      if (error instanceof AxiosError && error.response) {
        if (error.response.status === 400) {
          console.error('User login failed! Please try again later.')
        }
        if (error.response.status === 401) {
          console.error('Invalid credentials!')
        }
        if (error.response.status === 404) {
          console.error('Username/email not found!')
        }
      } else {
        console.error('An unexpected error occurred:', error)
      }
    })
}

const closeModal = () => {
  emits('close')
}

const handleInput = () => {
  goalAmount.value = Math.max(0, Math.round(goalAmount.value))
}
</script>
