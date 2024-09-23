<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gray-50 px-6 py-8 mt-12">
    <h1 class="text-2xl font-bold mb-10">{{ textData.spendingCategories.title }}</h1>

    <ul class="w-full max-w-lg space-y-4">
      <li
        v-for="category in categories"
        :key="category.name"
        class="flex flex-row-reverse justify-between items-center border-b pb-2"
      >
        <input
          type="checkbox"
          :value="category.name"
          v-model="selectedCategories"
          class="h-5 w-5 text-red-500 border-gray-300 rounded focus:ring-2"
        />
        <span class="flex-1 text-left mr-4">{{ category.norwegianText }}</span>
      </li>
    </ul>

    <button
      @click="goToNext"
      :disabled="selectedCategories.length !== 3"
      class="mt-8 px-6 py-2 rounded bg-red-500 text-white disabled:bg-gray-300"
    >
      {{ textData.spendingCategories.submitButtonText }}
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import textData from '@/assets/text.no.json'
import api from '@/services/enumService/index'
import type { Category } from '@/enums/enums'
import type { AxiosResponse } from 'axios'
import { AxiosError } from 'axios'

const categories = ref([] as Category[])
const selectedCategories = ref<string[]>([])

/**
 * Fetches the spending categories from the API
 */
const fetchCategories = async () => {
  try {
    const response: AxiosResponse<Category[]> = await api.getCategories()
    const data = response.data

    if (response.status === 200) {
      categories.value = data
    }
  } catch (error: unknown) {
    if (error instanceof AxiosError && error.response) {
      if (error.response.status === 400) {
        console.error(error)
      }
    }
  }
}

const goToNext = () => {
  if (selectedCategories.value.length === 3) {
    console.log('Valgte kategorier:', selectedCategories.value)
  }
}

onMounted(fetchCategories)
</script>
