<template>
  <div
    class="min-h-screen bg-backgroundImage bg-cover bg-no-repeat bg-center flex flex-col items-center justify-start px-6 py-8"
  >
    <div
      class="w-full max-w-lg bg-white p-6 rounded-lg shadow-lg border border-gray-300 mt-20 text-primary-text mt-20"
    >
      <h1 class="text-2xl lex justify-center text-center font-bold mb-10">
        {{ textData.interests.title }}
      </h1>

      <ul class="w-full max-w-lg space-y-4">
        <li
          v-for="interest in interests"
          :key="interest.name"
          class="flex flex-row-reverse justify-between items-center border-b pb-2"
        >
          <input
            type="checkbox"
            :value="interest.name"
            v-model="selectedInterests"
            class="h-5 w-5 border-gray-300 rounded focus:ring-2"
          />
          <span class="flex-1 text-left mr-4">{{ interest.norwegianText }}</span>
        </li>
      </ul>

      <button
        @click="handleNext"
        :disabled="selectedInterests.length !== 3"
        class="mt-8 px-6 py-2 rounded flex justify-center w-full bg-primaryBtn hover:bg-hooverBtn text-white disabled:bg-gray-300"
      >
        {{ textData.interests.submitButtonText }}
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineEmits } from 'vue'
import textData from '@/assets/text.no.json'
import api from '@/services/enumService/index'
import type { Interest } from '@/enums/enums'

const emit = defineEmits(['update-insights', 'next'])

const interests = ref([] as Interest[])
const selectedInterests = ref<string[]>([])

/**
 * Fetches the interests from the API and sets the interest ref.
 */
const fetchInterests = async () => {
  try {
    const response = await api.getInterests()
    if (Array.isArray(response.data)) {
      interests.value = response.data
    } else {
      console.error('Unexpected data format:', response.data)
    }
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}

/**
 * Emits next event if three interests are selected.
 */
const handleNext = () => {
  if (selectedInterests.value.length === 3) {
    emit('update-insights', {
      interests: selectedInterests.value
    })
    emit('next')
  }
}

onMounted(fetchInterests)
</script>
