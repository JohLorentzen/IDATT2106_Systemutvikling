<template>
  <div
    class="min-h-screen bg-backgroundImage bg-cover bg-no-repeat bg-center p-6 rounded-lg shadow-lg border border-gray-300 flex flex-col items-center justify-start px-6 py-8"
  >
    <div
      class="w-full max-w-lg bg-white p-6 rounded-lg shadow-lg border border-gray-300 mt-20 text-primary-text"
    >
      <h1 class="text-2xl lex justify-center text-center font-bold mb-10">
        {{ textData.spendingCategories.title }}
      </h1>

      <ul class="w-full max-w-lg space-y-4 overflow-y-auto" style="max-height: 350px">
        <li
          v-for="category in categories"
          :key="category.name"
          class="flex flex-row-reverse justify-between items-center border-b pb-2"
        >
          <input
            type="checkbox"
            :value="category.name"
            v-model="selectedCategories"
            class="h-5 w-5 border-gray-300 rounded"
          />
          <span class="flex-1 text-left mr-4">{{ category.norwegianText }}</span>
        </li>
      </ul>

      <button
        @click="handleNext"
        :disabled="selectedCategories.length !== 3"
        class="mt-8 px-6 py-2 w-full rounded bg-primaryBtn hover:bg-hooverBtn text-white disabled:bg-gray-300"
      >
        {{ textData.spendingCategories.submitButtonText }}
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineEmits } from 'vue'
import textData from '@/assets/text.no.json'
import api from '@/services/enumService/index'
import type { Category } from '@/enums/enums'

const emit = defineEmits(['update-insights', 'complete-survey'])

const categories = ref([] as Category[])
const selectedCategories = ref<string[]>([])

const fetchCategories = async () => {
  const response = await api.getCategories()
  categories.value = response.data
}

/**
 * Handles the next button click on the survey.
 */
const handleNext = () => {
  if (selectedCategories.value.length === 3) {
    emit('update-insights', {
      categories: selectedCategories.value
    })
    emit('complete-survey')
  }
}
onMounted(fetchCategories)
</script>
