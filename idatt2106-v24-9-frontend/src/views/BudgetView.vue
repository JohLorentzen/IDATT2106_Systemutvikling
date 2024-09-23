<template>
  <div class="min-h-screen bg-background bg-cover bg-no-repeat">
    <div class="py-32 px-6">
      <h2 class="text-3xl text-primary-text tracking-wider font-bold text-center">
        Månedlig budsjett
      </h2>
      <div class="flex justify-center items-center w-full md:w-1/2 mx-auto gap-2 mt-10">
        <button
          @click="decrementYear"
          class="hover:bg-primary-text hover:text-white font-bold py-1 px-2 rounded text-xl text-primary-text"
        >
          <i class="fa fa-arrow-left"></i>
        </button>
        <h1 class="text-lg font-bold text-center text-primary-text">{{ selectedYear }}</h1>
        <button
          @click="incrementYear"
          class="hover:bg-primary-text hover:text-white font-bold py-1 px-2 rounded text-xl text-primary-text"
        >
          <i class="fa fa-arrow-right"></i>
        </button>
      </div>
      <div class="flex justify-center items-center w-full md:w-1/2 mx-auto gap-2">
        <button
          @click="decrementMonth"
          class="hover:bg-primary-text hover:text-white font-bold py-1 px-2 rounded text-xl text-primary-text"
        >
          <i class="fa fa-arrow-left"></i>
        </button>
        <h1 class="text-3xl font-bold text-center text-primary-text">
          {{ monthName(selectedMonth) }}
        </h1>
        <button
          @click="incrementMonth"
          class="hover:bg-primary-text hover:text-white font-bold py-1 px-2 rounded text-xl text-primary-text"
        >
          <i class="fa fa-arrow-right"></i>
        </button>
      </div>
      <h4 class="text-xl text-center mt-4 text-primary-text">
        <b>{{ formatNumber(budget.totalSpentSum) }} kr</b> /
        {{ formatNumber(budget.totalBudgetedSum) }} kr
      </h4>
      <h2 class="text-xl text-center mt-4 mb-4 text-primary-text">
        Gjenstående: <b>{{ formatNumber(budget.totalBudgetedSum - budget.totalSpentSum) }}kr</b>
      </h2>

      <div class="flex justify-center">
        <button
          @click="handleCreateNewPostClick"
          class="bg-white text-primary-text hover:bg-gray-200 px-6 py-2 transition-colors duration-150 rounded-full flex items-center justify-center"
          style="box-shadow: 0px 10px 15px rgba(0, 0, 0, 0.3)"
        >
          Opprett ny post <span class="text-4xl ml-3"> +</span>
        </button>
      </div>

      <div class="flex flex-wrap justify-center mt-6 container mx-auto">
        <BudgetCard
          :id="budgetPost.id"
          v-for="budgetPost in budget.budgetPosts"
          :key="budgetPost.id"
          :budget-post="budgetPost"
          :category="findCategory(budgetPost.category)"
          @selectCard="handleCardClick"
        />
      </div>
    </div>

    <CreateBudgetPostModal
      v-if="isModalOpen"
      @close="handleCloseModal"
      :is-create-new="createNewPost"
      :budget-post="selectedBudgetPost"
      :budget-id="budget.id"
    />
  </div>
</template>

<script setup lang="ts">
import CreateBudgetPostModal from '@/components/CreateBudgetPostModal.vue'
import { onMounted, ref, watch } from 'vue'
import type { Category } from '@/enums/enums'
import BudgetCard from '@/components/BudgetCard.vue'
import api from '@/services/bugdetService'
import apiEnums from '@/services/enumService'
import type { BudgetDTO, BudgetPostDTO } from '@/services/bugdetService/types'
import { formatNumber } from '@/util/NumberFormatter'

const selectedYear = ref(2024)
const selectedMonth = ref(5)
const selectedBudgetPost = ref({} as BudgetPostDTO)
const isModalOpen = ref(false)
const createNewPost = ref(false)
const findCategory = (categoryName: string) => {
  return (
    categories.value.find((category) => category.name === categoryName) ?? {
      name: 'NOT_CATEGORIZED',
      norwegianText: 'Ikke kategorisert',
      englishText: 'Not categorized'
    }
  )
}

const handleCreateNewPostClick = () => {
  createNewPost.value = true
  selectedBudgetPost.value = { category: '', budgetedSum: 0, actualSum: 0 }
  isModalOpen.value = true
}
const handleCardClick = (data: BudgetPostDTO) => {
  createNewPost.value = false
  selectedBudgetPost.value = data
  isModalOpen.value = true
}

const handleCloseModal = () => {
  fetchBudgetPosts()
}

const categories = ref([] as Category[])

const budget = ref({} as BudgetDTO)

const fetchCategories = async () => {
  const response = await apiEnums.getCategories()
  categories.value = response.data
}

onMounted(() => {
  fetchCategories()
  fetchBudgetPosts()
})

const incrementYear = () => {
  selectedYear.value++
}

const decrementYear = () => {
  selectedYear.value--
}

const incrementMonth = () => {
  if (selectedMonth.value === 12) {
    selectedMonth.value = 1
    incrementYear()
  } else {
    selectedMonth.value++
  }
}

watch(selectedYear, () => {
  fetchBudgetPosts()
})
watch(selectedMonth, () => {
  fetchBudgetPosts()
})

/**
 * Fetches the budget posts for the selected month and year.
 */
const fetchBudgetPosts = async () => {
  const response = await api.getBudgetByMonthAndYear(selectedMonth.value, selectedYear.value)
  budget.value = response.data

  setTimeout(() => {
    isModalOpen.value = false
  }, 100)
}

const decrementMonth = () => {
  if (selectedMonth.value === 1) {
    selectedMonth.value = 12
    decrementYear()
  } else {
    selectedMonth.value--
  }
}

const monthNames = [
  'Januar',
  'Februar',
  'Mars',
  'April',
  'Mai',
  'Juni',
  'Juli',
  'August',
  'September',
  'Oktober',
  'November',
  'Desember'
]

const monthName = (month: number) => monthNames[month - 1]
</script>
