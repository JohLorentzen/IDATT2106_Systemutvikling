<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gray-50 px-6 py-8">
    <h1 class="text-2xl font-bold mb-10">{{ textData.interests.title }}</h1>

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
          class="h-5 w-5 text-red-500 border-gray-300 rounded focus:ring-2 focus:ring-red-500"
        />
        <span class="flex-1 text-left mr-4">{{ interest.norwegianText }}</span>
      </li>
    </ul>

    <button
      @click="goToNext"
      :disabled="selectedInterests.length !== 3"
      class="mt-8 px-6 py-2 rounded bg-red-500 text-white disabled:bg-gray-300"
    >
      {{ textData.interests.submitButtonText }}
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import textData from '@/assets/text.no.json'
import { useRouter } from 'vue-router'
import api from '@/services/enumService/index'
import type { Interest } from '@/enums/enums'

const router = useRouter()
const interests = ref([] as Interest[])
const selectedInterests = ref<string[]>([])

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

const goToNext = () => {
  if (selectedInterests.value.length === 3) {
    router.push('/spendingCategories')
  }
}

onMounted(fetchInterests)
</script>
