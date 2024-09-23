<template>
  <div class="min-h-screen bg-background bg-cover bg-no-repeat">
    <div class="py-32 px-6">
      <h2 class="text-3xl text-primary-text tracking-wider font-bold text-center">
        Utforsk Investeringsråd
      </h2>

      <div class="flex justify-center gap-4 my-4 mt-16 text-lg">
        <button
          v-for="tab in tabs"
          :key="tab.name"
          :class="{
            'text-primaryBtn border-b-4 border-primaryBtn': activeTab === tab.name,
            'text-primary-text hover:text-hoverBtn': activeTab !== tab.name
          }"
          class="px-6 py-2 transition duration-150 focus:outline-none"
          @click="setActiveTab(tab.name)"
        >
          {{ tab.label }}
        </button>
      </div>

      <div v-if="activeTab === 'interests'" class="mb-8 mt-8">
        <div class="flex flex-wrap max-w-4xl w-full mx-auto gap-6">
          <div
            v-for="category in groupedCategories.yourInterests"
            :key="category.title"
            class="p-4 shadow-lg rounded-lg flex flex-col md:flex-[1_1_12rem] md:max-w-72 flex-[1_1_10rem] justify-between bg-white"
          >
            <h3 class="font-semibold text-lg text-primary-text">{{ category.title }}</h3>
            <p class="text-sm text-primaryBtn font-bold">{{ category.interest_text }}</p>
            <p class="text-sm text-gray-500 italic">- {{ category.source }}</p>
            <button
              @click="openModal(category)"
              class="bg-primary-text hover:bg-primaryBtn text-white font-bold py-2 px-4 rounded text-xs md:text-sm mt-4"
            >
              Les Mer
            </button>
          </div>
        </div>
      </div>
      <div v-else class="mb-8 mt-8">
        <div class="flex max-w-4xl flex-wrap w-full mx-auto gap-6">
          <div
            v-for="category in groupedCategories.allTips"
            :key="category.title"
            class="p-4 shadow-lg rounded-lg flex flex-col md:flex-[1_1_12rem] md:max-w-72 flex-[1_1_10rem] justify-between bg-white"
          >
            <h3 class="font-semibold text-lg text-primary-text">{{ category.title }}</h3>
            <p class="text-sm text-primaryBtn font-bold">{{ category.interest_text }}</p>
            <p class="text-sm text-gray-500 italic">- {{ category.source }}</p>
            <button
              @click="openModal(category)"
              class="bg-primary-text hover:bg-primaryBtn text-white font-bold py-2 px-4 rounded text-xs md:text-sm mt-4"
            >
              Les Mer
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <transition name="fade">
    <div
      v-if="selectedCategory && showModal"
      class="fixed inset-0 bg-background bg-opacity-50 overflow-y-auto h-full w-full px-4 mt-5"
    >
      <div class="relative top-20 mx-auto p-5 border md:w-96 w-76 shadow-lg rounded-md bg-white">
        <div class="mt-3 text-center">
          <h3 class="text-lg leading-6 font-medium text-gray-900">{{ selectedCategory.title }}</h3>
          <div class="mt-2 px-3 py-3">
            <p class="text-sm text-gray-500">{{ selectedCategory.description }}</p>
            <p class="text-sm text-gray-500 italic mt-5">- {{ selectedCategory.source }}</p>
          </div>
          <div class="items-center px-4 py-3">
            <button
              @click="showModal = false"
              class="px-4 py-2 bg-primaryBtn text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-300"
            >
              Lukk
            </button>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'

import categoriesData from '@/assets/categories.json'
import api from '@/services/userService'

const yourInterests = ref<Array<any>>([])
const viewMode = ref('interests')
const showModal = ref(false)
const selectedCategory = ref(null as InterestCategory | null)

const tabs = [
  { name: 'interests', label: 'Dine Interesser' },
  { name: 'all', label: 'Andre Investeringsråd' }
]
const activeTab = ref(tabs[0].name)

const setActiveTab = (tabName: string) => {
  activeTab.value = tabName
  viewMode.value = tabName
}

onMounted(() => {
  fetchUserInsights()
})

/**
 * Fetches user insights from the API to determine the user's interests.
 */
const fetchUserInsights = async () => {
  try {
    const response = await api.getUserInsight()
    yourInterests.value = response.data.interests
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}

const categories = reactive(categoriesData as InterestCategory[])
type InterestCategory = {
  title: string
  description: string
  source: string
  interest: string
  interest_text: string
}

/**
 * Groups the categories into two groups: your interests and all tips.
 */
const groupedCategories = computed(() => {
  const allGroups = { yourInterests: [] as InterestCategory[], allTips: [] as InterestCategory[] }
  categories.forEach((category: InterestCategory) => {
    if (yourInterests.value.includes(category.interest)) {
      allGroups.yourInterests.push(category)
    } else {
      allGroups.allTips.push(category)
    }
  })
  return allGroups
})

const openModal = (category: InterestCategory) => {
  selectedCategory.value = category
  showModal.value = true
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
