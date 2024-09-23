<template>
  <NavBar v-if="isLoggedIn && hasUserInsight" />
  <RouterView />
</template>
<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
import { onMounted, ref, watch } from 'vue'
import { useUserStore } from './stores/userStore'
import api from '@/services/userService'

const isLoggedIn = ref(false)
const hasUserInsight = ref(false)
const userStore = useUserStore()

watch(
  () => userStore.isLoggedIn,
  (newVal, oldVal) => {
    isLoggedIn.value = newVal
    hasUserInsight.value = userStore.hasUserInsight
    if (newVal && !oldVal) {
      fetchUserInsights()
    }
  },
  { immediate: true }
)

watch(
  () => userStore.hasUserInsight,
  (newVal) => {
    hasUserInsight.value = newVal
  },
  { immediate: true }
)

const fetchUserInsights = async () => {
  try {
    if (!userStore.isLoggedIn) return
    const response = await api.getUserInsight()
    hasUserInsight.value = response.status !== 204
  } catch (error) {
    console.error('Failed to fetch user insights', error)
    hasUserInsight.value = false
  }
}

onMounted(fetchUserInsights)
</script>
<style>
main {
  margin: 0 0 5rem;
  padding: 0;
}
@media (min-width: 768px) {
  main {
    margin-top: 5rem;
    margin-bottom: 0;
  }
}
</style>
