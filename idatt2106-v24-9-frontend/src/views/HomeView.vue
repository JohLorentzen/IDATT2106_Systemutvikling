<template>
  <div
    class="min-h-screen flex flex-col items-center justify-center bg-backgroundImage bg-cover bg-no-repeat bg-center sm:pb-4 pb-24"
  >
    <ToastComponent :position="toastPosition" />
    <div v-show="hasNoSavingPath">
      <div>
        <h1 class="font-bold text-xl">Du har ingen aktive sparemål enda.</h1>
        <RouterLink
          to="/savingGoals"
          class="mt-6 px-10 py-2 bg-primaryBtn right-1 text-white rounded hover:bg-hooverBtn transition duration-300 ease-in-out focus:outline-none shadow-lg block mx-auto text-center"
          >Mine sparemål</RouterLink
        >
      </div>
    </div>
    <div v-show="!hasNoSavingPath" class="flex flex-col items-center md:mt-28 md:w-96 w-72 mt-2">
      <div>
        <!-- FontAwesome Icon -->
        <font-awesome-icon
          :icon="['fas', 'trophy']"
          class="text-custom-yellow sm:text-6xl stroke-orange-500 stroke-2 text-4xl"
          style="transition: 0.6s; filter: drop-shadow(2px 4px 6px rgba(0, 0, 0, 0.2))"
        />
      </div>
      <svg
        class="w-auto h-auto max-h-[50vh] max-w-[80vw] md:max-w-lg"
        viewBox="0 0 292 483"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        style="transform: scale(1, -1)"
      >
        <defs>
          <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" style="stop-color: #00ce52; stop-opacity: 1" />
            <stop offset="100%" style="stop-color: #00b4d8; stop-opacity: 1" />
          </linearGradient>
          <filter id="shadow" x="-20%" y="-20%" width="140%" height="140%">
            <feDropShadow dx="2" dy="4" stdDeviation="3" />
          </filter>
        </defs>

        <g filter="url(#shadow)">
          <path
            d="M19 15V93.0735C19 109.642 32.4315 123.073 49 123.073H243C259.569 123.073 273 136.505 273 153.073V190.148C273 206.717 259.569 220.148 243 220.148H49C32.4315 220.148 19 233.58 19 250.148V290.571C19 307.139 32.4315 320.571 49 320.571H243C259.569 320.571 273 334.002 273 350.571V382.385C273 398.954 259.569 412.385 243 412.385H49C32.4315 412.385 19 425.817 19 442.385V459.136"
            stroke="white"
            stroke-width="30"
            stroke-linecap="round"
          />
        </g>

        <path
          ref="fillablePath"
          d="M19 15V93.0735C19 109.642 32.4315 123.073 49 123.073H243C259.569 123.073 273 136.505 273 153.073V190.148C273 206.717 259.569 220.148 243 220.148H49C32.4315 220.148 19 233.58 19 250.148V290.571C19 307.139 32.4315 320.571 49 320.571H243C259.569 320.571 273 334.002 273 350.571V382.385C273 398.954 259.569 412.385 243 412.385H49C32.4315 412.385 19 425.817 19 442.385V459.136"
          stroke="url(#gradient)"
          stroke-width="20"
          stroke-linecap="round"
          :stroke-dasharray="pathLength"
          :stroke-dashoffset="pathLength * (1 - progress / 100)"
          style="transition: stroke-dashoffset 0.6s ease-out"
        />
        <path
          class="star"
          v-for="challenge in savingPath.completedChallenges"
          :key="challenge.id !== null ? challenge.id : undefined"
          d="M-10,-15 L-12,-22 L-20,-22 L-13,-26 L-15,-34 L-10,-29 L-5,-34 L-7,-26 L0,-22 L-8,-22 L-10,-15"
          :transform="
            challenge.cx && challenge.cy
              ? 'translate(' + (challenge.cx + 25) + ', ' + (challenge.cy + 65) + ') scale(2.5)'
              : ''
          "
          stroke="orange"
          stroke-width="0.75"
          fill="gold"
          @click="showPopup(challenge)"
          cursor="pointer"
        />
      </svg>
      <div
        v-if="showDescription"
        class="absolute top-0 left-0 w-full h-full flex items-center justify-center p-3 bg-white bg-opacity-80"
        @click="showDescription = false"
      >
        <div class="bg-white rounded-lg pt-6 pb-3 px-4 shadow-lg relative"
          @click.stop
        >
          <FontAwesomeIcon
            icon="xmark"
            class="fa-xl absolute top-4 right-4 text-gray-500 hover:text-gray-800 cursor-pointer"
            @click="showDescription = false"
          />
          <h2 class="text-xl font-bold">{{ clickedChallenge?.challenge.name }}</h2>
          <p>{{ clickedChallenge?.challenge.description }}</p>
          <p class="text-sm text-gray-500 mt-2">
            Du sparte {{ clickedChallenge?.savedAmount }}kr på denne utfordringen
          </p>
          <button @click="showDescription = false" class="mt-4 px-2 py-1 text-red-500 block mx-auto border border-red-500">Lukk</button>
        </div>
      </div>

      <ToastComponent :position="toastPosition" />

      <div class="w-full max-w-xs mx-auto">
        <SavingGoalOverview
          :goal="savingPath.savingsGoal"
          :is-completed="false"
          @open-modal="isModalOpen = true"
          @open-deposit-modal="isDepositModalOpen = true"
        />
        <EditSavingGoalModal
          :isModalOpen="isModalOpen"
          :isNewGoal="false"
          :goal="savingPath.savingsGoal"
          @close="isModalOpen = false"
          @fetch-goals="calculatePositions"
        />
        <SavingGoalDepositModal
          v-if="isDepositModalOpen"
          @close="isDepositModalOpen = false"
          @make-deposit="updateSavedAmount"
        />
        <CompletedGoalModal
          v-if="isCompletedGoalModalOpen"
          :isReached="isGoalReached"
          :savedAmount="savingPath.savingsGoal.savedAmount"
          @close="isCompletedGoalModalOpen = false"
        />
        <button
          class="hidden mt-6 px-10 py-2 bg-white right-1 text-primary-text rounded-2xl hover:bg-gray-200 transition duration-300 ease-in-out focus:outline-none shadow-lg md:block mx-auto text-center mb-8"
          @click="router.push('/savingGoals')"
        >
          {{ t.mySavingGoals }}
          <font-awesome-icon icon="arrow-right" />
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type {
  CompletedUserChallenge,
  SavingsPath,
  UserChallenge
} from '@/services/savingPath/types'
import type { AxiosResponse } from 'axios'
import savingsGoalApi from '@/services/savingPath'
import SavingGoalOverview from '@/components/SavingGoalOverview.vue'
import EditSavingGoalModal from '@/components/EditSavingGoalModal.vue'
import CompletedGoalModal from '@/components/CompletedGoalModal.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import t from '@/assets/text.no.json'
import router from '@/router'
import SavingGoalDepositModal from '@/components/SavingGoalDepositModal.vue'
import { useToast } from 'primevue/usetoast'

const toast = useToast()
const toastPosition = computed(() => {
  return window.innerWidth < 768 ? 'top-center' : 'bottom-right'
})

const isCompletedGoalModalOpen = ref(false)
const isGoalReached = ref(false)
const isModalOpen = ref(false)
const isDepositModalOpen = ref(false)
const fillablePath = ref<SVGPathElement | null>(null)
const pathLength = ref(0)
const hasNoSavingPath = ref(true)
const savingPath = ref<SavingsPath>({
  completedChallenges: [] as CompletedUserChallenge[],
  savingsGoal: {
    id: null,
    title: '',
    goalAmount: 0,
    isTargetReached: false,
    savedAmount: 0,
    endDate: new Date(),
    active: false,
    daysLeft: 0
  }
})

const clickedChallenge = ref<UserChallenge | null>(null)
const showDescription = ref(false)

/**
 * Progress of the passed based on the saved amount as a percentage
 */
const progress = computed(() => {
  const goalAmount = savingPath.value.savingsGoal.goalAmount
  const savedAmount = savingPath.value.savingsGoal.savedAmount
  return goalAmount > 0 ? (savedAmount / goalAmount) * 100 : 0
})

/**
 * Shows popup
 * @param desc string description of challenge
 */
function showPopup(challange: CompletedUserChallenge) {
  clickedChallenge.value = challange.userChallenge
  showDescription.value = true
}

const calculatePositions = async () => {
  try {
    const res: AxiosResponse<SavingsPath> = await savingsGoalApi.getSavingsPath()
    if (res.status === 204) {
      hasNoSavingPath.value = true
      return
    }

    savingPath.value = res.data
    if (fillablePath.value) {
      pathLength.value = fillablePath.value.getTotalLength()
      savingPath.value.completedChallenges.forEach((challenge) => {
        const position = fillablePath?.value?.getPointAtLength(
          pathLength.value * (challenge.pointOfCompletion / savingPath.value.savingsGoal.goalAmount)
        )
        if (!position) return
        challenge.cx = position.x
        challenge.cy = position.y
      })
    }
    hasNoSavingPath.value = false
  } catch (e) {
    console.log('Error fetching savings path: ' + e)
    hasNoSavingPath.value = true
  }
}

/**
 * Updates saved amount by adding the deposit to it if the post request is successful.
 * Toast will display if deposit was successful.
 * If not, nothing will happen and a toast will display the error.
 *
 * @param deposit - the amount emitted from SavingGoalDepositModal
 */
async function updateSavedAmount(deposit: number) {
  try {
    savingPath.value.savingsGoal.savedAmount += deposit
    const response = await savingsGoalApi.postActiveSavingsGoal(savingPath.value.savingsGoal)
    if (response.status === 201) {
      toast.add({
        severity: 'success',
        summary: `${deposit} kroner lagt til i sparemålet`,
        life: 3000
      })
    } else {
      toast.add({
        severity: 'error',
        summary: `Problemer med å legge til beløp, prøv igjen.`,
        life: 4000
      })
    }
  } catch (e) {
    toast.add({
      severity: 'error',
      summary: `Problemer med serveren, prøv igjen senere.`,
      life: 4000
    })
  } finally {
    isGoalCompleted()
  }
}

/**
 * Checks if the goal is completed or reached.
 * If the goal is reached, the isGoalReached is set to true and the completedGoalModal is opened.
 * If the goal is completed, but not reached, the completedGoalModal is opened with a different message.
 */
function isGoalCompleted() {
  const goalAmount: number = savingPath.value.savingsGoal.goalAmount
  const savedAmount: number = savingPath.value.savingsGoal.savedAmount
  const daysLeft: number = savingPath.value.savingsGoal.daysLeft
  isGoalReached.value = false
  isCompletedGoalModalOpen.value = false
  if (savedAmount >= goalAmount) {
    // Goal reached
    isGoalReached.value = true
    isCompletedGoalModalOpen.value = true
  } else if (daysLeft <= 0) {
    // Goal completed, but not reached
    isCompletedGoalModalOpen.value = true
  }
}

onMounted(() => {
  calculatePositions()
})
</script>
