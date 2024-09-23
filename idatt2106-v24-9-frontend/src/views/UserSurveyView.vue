<template>
  <div>
    <component
      :is="currentComponent"
      @update-insights="handleUpdateInsights"
      @next="nextComponent"
      @complete-survey="completeSurvey"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/userStore'
import UserIntroduction from '@/components/UserIntroduction.vue'
import UserInterests from '@/components/UserInterests.vue'
import type { UserInsight } from '@/services/userService/types'
import SpendingCategories from '@/components/SpendingCategories.vue'
import api from '@/services/userService'
import router from '@/router'

const components = [UserIntroduction, UserInterests, SpendingCategories]

const currentComponentIndex = ref(0)
const currentComponent = computed(() => components[currentComponentIndex.value])

const defaultInsights: UserInsight = {
  id: null,
  skillLevel: {} as any,
  lifeSituation: {} as any,
  interests: [],
  categories: []
}

const userInsights = ref<UserInsight>(defaultInsights)

const userStore = useUserStore()

const nextComponent = () => {
  if (currentComponentIndex.value < components.length - 1) {
    currentComponentIndex.value += 1
  }
}

const handleUpdateInsights = (data: any) => {
  Object.assign(userInsights.value, data)
}

/**
 * Saves the user insights to the user object in the user store
 */
async function completeSurvey() {
  userStore.saveUserInsightToUser(userInsights.value)
  userStore.hasUserInsight = true
  api.postUserInsight(userInsights.value).then(() => {
    router.push('/')
  })
}

defineExpose({
  currentComponent,
  userInsights,
  nextComponent,
  handleUpdateInsights,
  completeSurvey
})
</script>
