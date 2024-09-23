<template>
  <div class="user-challenge bg-white border border-gray-300 rounded-lg mb-4 w-full">
    <div
      class="challenge-summary cursor-pointer grid grid-cols-1 md:grid-cols-3 items-center text-center gap-4 p-2"
      @click="toggleExpand"
    >
      <div class="space-y-2">
        <div class="progress-wheel relative h-24 w-24 mx-auto">
          <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <circle cx="50" cy="50" r="30" fill="none" stroke="#f3f3f3" stroke-width="5"></circle>
            <circle
              :stroke-dasharray="circumference"
              :stroke-dashoffset="offset"
              cx="50"
              cy="50"
              r="30"
              fill="none"
              :stroke="color"
              stroke-width="5"
              transform="rotate(-90, 50, 50)"
            ></circle>
          </svg>
          <span
            :class="{
              'text-gray-500 text-2xl': userChallenge.savedAmount < userChallenge.goalAmount,
              'text-yellow-500 text-2xl': userChallenge.savedAmount >= userChallenge.goalAmount
            }"
            class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2"
            >&#9733;</span
          >
        </div>
        <p class="text-sm text-gray-700">
          <span class="font-bold">{{ formatNumber(userChallenge.savedAmount) }} kr</span> /
          {{ formatNumber(userChallenge.goalAmount) }} kr
        </p>
      </div>

      <div class="md:col-span-2 space-y-2">
        <h3 class="text-lg font-bold">{{ userChallenge.challenge.name }}</h3>
        <div class="text-sm text-gray-700">
          <span class="font-bold">{{ timeLeft.number }}</span> {{ timeLeft.unit }}
        </div>
      </div>
    </div>

    <div v-show="expanded" class="flex flex-col items-center justify-center details mt-2 p-4">
      <p class="text-sm text-gray-700">{{ userChallenge.challenge.description }}</p>
      <div class="mt-4 p-3 bg-gray-100 rounded-lg flex items-center justify-center">
        <font-awesome-icon
          :icon="['fas', 'lightbulb']"
          class="text-yellow-500"
          style="font-size: 24px"
        />
        <p class="text-gray-800 text-sm ml-2">Tips: {{ userChallenge.challenge.tip }}</p>
      </div>
    </div>

    <div class="expand-indicator text-center py-1 cursor-pointer" @click.stop="toggleExpand">
      {{ expanded ? '▲' : '▼' }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { UserChallenge } from '@/services/savingPath/types'
import type { Ref } from 'vue'
import { formatNumber } from '@/util/NumberFormatter'

const props = defineProps<{
  userChallenge: UserChallenge
}>()

const expanded: Ref<boolean> = ref(false)
const daysLeft: Ref<number | null> = ref(null)

/**
 * Computed timeLeft that returns an object with the number of days or hours left.
 */
const timeLeft = computed(() => {
  let number, unit

  if (daysLeft.value && daysLeft.value >= 1) {
    number = daysLeft.value
    unit = daysLeft.value === 1 ? 'dag' : 'dager'
  } else if (daysLeft.value !== null) {
    number = Math.floor(daysLeft.value * 24)
    unit = number === 1 ? 'time' : 'timer'
  }

  return {
    number: number !== undefined ? number.toString() : '',
    unit: unit ? ` ${unit} igjen` : ''
  }
})

const circumference = computed(() => 2 * Math.PI * 45)

const offset = computed(() => {
  const percent = Math.min(
    (props.userChallenge.savedAmount / props.userChallenge.goalAmount) * 100,
    100
  )
  return circumference.value * (1 - percent / 100)
})

const color = computed(() => '#4CAF50')

const toggleExpand = () => {
  expanded.value = !expanded.value
}

/**
 * Calculates the number of days left until the challenge ends.
 */
const calculateDaysLeft = () => {
  const endDate = new Date(props.userChallenge.toDate)
  const today = new Date()
  if (!endDate || today > endDate) {
    daysLeft.value = 0
  } else {
    const millisecondsInDay = 1000 * 60 * 60 * 24
    daysLeft.value = Math.floor((endDate.getTime() - today.getTime()) / millisecondsInDay)
  }
}

onMounted(calculateDaysLeft)
</script>
