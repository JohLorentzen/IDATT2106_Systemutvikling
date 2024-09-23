<template>
  <div
    class="challenge-container text-primary-text min-h-screen bg-background bg-waveBackground bg-scroll bg-no-repeat bg-top flex flex-col items-center w-full justify-start"
  >
    <div class="challenge-heading font-bold text-center mb-8 mt-28">
      <ToastComponent :position="toastPosition" />
      <h2 class="text-3xl text-white tracking-wider">Utfordringer</h2>
    </div>

    <!-- New Challenge Button: Round with Plus sign -->
    <button
      class="bg-white text-primary-text hover:bg-gray-200 px-6 py-2 transition-colors duration-150 rounded-full flex items-center justify-center"
      @click="takeOnNewChallenge"
      style="box-shadow: 0px 10px 15px rgba(0, 0, 0, 0.3)"
    >
      {{ text.generateNewChallengeBtnText }} <span class="text-4xl ml-3"> +</span>
    </button>

    <!-- Tab Buttons: 'Aktive' and 'Fullførte' -->
    <div class="flex justify-center gap-4 my-4 mt-16 text-lg">
      <button
        v-for="tab in tabs"
        :key="tab.name"
        :class="{
          'text-primaryBtn border-b-4 border-primaryBtn border-hoverBtn': activeTab === tab.name,
          'text-primary-text hover:text-hoverBtn': activeTab !== tab.name
        }"
        class="px-6 py-2 transition duration-150 focus:outline-none"
        @click="setActiveTab(tab.name)"
      >
        {{ tab.label }}
      </button>
    </div>

    <div class="mb-20 w-full max-w-lg text-center">
      <div v-show="activeTab === 'active'">
        <div v-if="activeChallenges && activeChallenges.length">
          <div v-for="challenge in activeChallenges" :key="challenge.id || 'default-key'">
            <ChallengeCard :userChallenge="challenge" />
          </div>
        </div>
        <div v-else>
          <p>Ingen aktive utfordringer for øyeblikket. Lag en ny utfordring!</p>
        </div>
      </div>
      <div v-show="activeTab === 'completed'">
        <div v-if="completedChallenges && completedChallenges.length">
          <div v-for="challenge in completedChallenges" :key="challenge.id || 'fallbackKey'">
            <ChallengeCard :userChallenge="challenge" />
          </div>
        </div>
        <div v-else>
          <p>Ingen fullførte utfordringer for øyeblikket.</p>
        </div>
      </div>
    </div>
    <ChallengePopup v-if="showModal" @close="closeModal" @challengeAccepted="refreshChallenges" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useUserChallengesStore } from '@/stores/userChallengeStore'
import { useCompletedUserChallengesStore } from '@/stores/completedUserChallengeStore'
import api from '@/services/savingPath'
import type { UserChallenge } from '@/services/savingPath/types'
import ChallengePopup from '@/components/ChallengeModal.vue'
import ChallengeCard from '@/components/ChallengeCard.vue'
import text from '@/assets/text.no.json'
import { useToast } from 'primevue/usetoast'

interface Tab {
  name: string
  label: string
}

const tabs: Tab[] = [
  { name: 'active', label: 'Aktive' },
  { name: 'completed', label: 'Fullførte' }
]

const userChallengesStore = useUserChallengesStore()
const completedUserChallengesStore = useCompletedUserChallengesStore()
const activeChallenges = ref<UserChallenge[]>()
const completedChallenges = ref<UserChallenge[]>()
const loading = ref(true)
const showModal = ref(false)
const activeTab = ref('active')
const toast = useToast()
const screenWidth = ref(window.innerWidth)

const isGenerateButtonDisabled = computed(() => {
  return activeChallenges.value !== undefined && activeChallenges.value.length >= 5
})

/**
 * Fetches the active challenges from the API.
 */
async function fetchActiveChallenges() {
  loading.value = true
  try {
    const activeChallengesData: UserChallenge[] | undefined =
      await userChallengesStore.getUserChallenges()
    activeChallenges.value = activeChallengesData?.filter((challenge: UserChallenge) => {
      return challenge.goalAmount > challenge.savedAmount && new Date(challenge.toDate) > new Date()
    })
  } catch (error) {
    console.error('Failed to load active challenges:', error)
  } finally {
    loading.value = false
  }
}

/**
 * Fetches the completed challenges from the API.
 */
async function fetchCompletedChallenges() {
  loading.value = true
  try {
    const completedChallengesData = await completedUserChallengesStore.getCompletedUserChallenges()
    completedChallenges.value = completedChallengesData
      ? completedChallengesData.map((c) => c.userChallenge)
      : []
  } catch (error) {
    console.error('Failed to load completed challenges:', error)
  } finally {
    loading.value = false
  }
}

function closeModal() {
  showModal.value = false
}

/**
 * Sets the active tab to the given tab name.
 * @param tabName The name of the tab to set as active.
 */
function setActiveTab(tabName: string) {
  activeTab.value = tabName
  if (tabName === 'active') {
    fetchActiveChallenges()
  } else if (tabName === 'completed') {
    fetchCompletedChallenges()
  }
}

/**
 * Handles the click event when the user wants to take on a new challenge.
 */
function takeOnNewChallenge() {
  if (isGenerateButtonDisabled.value) {
    // Show toast notification
    toast.add({
      severity: 'error',
      summary: 'Har allerde 5 aktive utfordringer',
      detail: 'Kan ikke ha mer enn 5 aktive utfordringer samtidig.',
      life: 5000
    })
  } else {
    showModal.value = true
  }
}

const toastPosition = computed(() => {
  return screenWidth.value < 768 ? 'top-center' : 'bottom-right'
})

onMounted(async () => {
  await api.calculateAmountSaved()
  await refreshChallenges()
})

async function refreshChallenges() {
  await fetchActiveChallenges()
  await fetchCompletedChallenges()
}
</script>
