<template>
  <div class="container mx-auto p-4 bg-white">
    <!-- Profile Header -->
  </div>
  <!-- User Info -->
  <div class="flex flex-col items-center mt-4">
    <div class="avatar relative inline-block">
      <div
        class="w-20 h-20 rounded-full p-1 bg-gradient-to-r bg-gradient-to-r from-pink-300 to-green-300 flex justify-center items-center"
      >
        <div class="bg-white rounded-full w-full h-full flex justify-center items-center">
          <font-awesome-icon icon="piggy-bank" class="w-16 h-16 text-primaryBtn" />
        </div>
      </div>
    </div>
    <h2 class="text-xl font-semibold mt-4">{{ userFullName }}</h2>
    <p class="text-sm text-gray-500">{{ userEmail }}</p>
  </div>

  <div class="p-4 mt-5 flex justify-center text-center flex-wrap">
    <div class="flex flex-col mr-5 items-center mb-2 md:mb-0">
      <p class="text-xl font-bold text-primary-text">{{ totalSavingGoalsCompleted }}</p>
      <p class="text-xs sm:text-sm text-gray-500">{{ text.savingGoalsCompleted }}</p>
    </div>
    <div class="flex flex-col items-center mr-5 mb-2 sm:mb-0">
      <p class="text-xl font-bold text-primary-text">{{ formatNumber(totalAmountSaved) }} kr</p>
      <p class="text-xs sm:text-sm text-gray-500">{{ text.totalMoneySaved }}</p>
    </div>
    <div class="flex flex-col items-center">
      <p class="text-xl font-bold text-primary-text">{{ totalChallengesCompleted }}</p>
      <p class="text-xs sm:text-sm text-gray-500">{{ text.challengesCompleted }}</p>
    </div>
  </div>
  <!-- List Items -->
  <div class="mt-4">
    <ul class="max-w-md mx-auto md:max-w-lg lg:max-w-xl flex flex-col gap-3">
      <li
        class="flex justify-between items-center px-4 py-2 border-b hover:bg-gray-100 cursor-pointer"
        @click="pushToSavingGoals"
      >
        <div class="flex items-center space-x-2">
          <font-awesome-icon icon="bullseye" style="color: #bb0239" />
          <p>{{ text.mySavingGoals }}</p>
        </div>
        <font-awesome-icon icon="chevron-right" />
      </li>
      <li
        class="flex justify-between items-center px-4 py-2 border-b hover:bg-gray-100 cursor-pointer mt-auto"
        @click="router.push('/explore')"
      >
        <div class="flex items-center space-x-2">
          <font-awesome-icon icon="graduation-cap" />
          <p>{{ text.exploreTips }}</p>
        </div>
        <font-awesome-icon icon="chevron-right" />
      </li>
      <li
        class="flex justify-between items-center px-4 py-2 border-b hover:bg-gray-100 cursor-pointer mt-auto"
        @click="logOut"
      >
        <div class="flex items-center space-x-2">
          <font-awesome-icon icon="arrow-right-from-bracket" />
          <p>{{ text.logout }}</p>
        </div>
        <font-awesome-icon icon="chevron-right" />
      </li>
    </ul>
  </div>
  <div
    v-if="showLogoutModal"
    class="bg-gray-400 fixed flex justify-center items-center inset-0 bg-opacity-75 z-50"
    @click="closeModal"
  >
    <div class="bg-white flex flex-col p-8 rounded relative" @click.stop>
      <FontAwesomeIcon
        icon="xmark"
        class="fa-xl absolute top-4 right-4 text-gray-500 hover:text-gray-800 cursor-pointer"
        @click="closeModal"
      />
      <h1 class="text-3xl font-bold text-gray-800">{{ text.logout }}</h1>
      <p class="mt-2">{{ text.logoutConfirmation }}</p>
      <div class="flex justify-center gap-1 mt-8">
        <button
          class="px-5 py-2 bg-primaryBtn right-1 text-white rounded hover:bg-hooverBtn transition duration-300 ease-in-out shadow-lg"
          @click="handleLogout"
        >
          {{ text.logout }}
        </button>
        <button
          class="bg-white text-red-600 rounded-md px-4 py-2 shadow-md border border-red-600 transition-all duration-200 hover:bg-gray-50"
          @click="closeModal"
        >
          {{ text.savingGoalDepositModalCloseBtn }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import text from '@/assets/text.no.json'
import router from '@/router'
import { useUserStore } from '@/stores/userStore'
import { onMounted, ref } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { formatNumber } from '@/util/NumberFormatter'

const showLogoutModal = ref<boolean>(false)
const userStore = useUserStore()
const totalAmountSaved = ref<number>(0)
const totalSavingGoalsCompleted = ref<number>(0)
const totalChallengesCompleted = ref<number>(0)
const userFullName = ref<string>()
const userEmail = ref<string>('')
onMounted(updateStats)

/**
 * Updates the user and stats for displaying on the profile page.
 */
async function updateStats() {
  await userStore.getUserStats()
  userFullName.value = userStore.currentUser.fullName?.valueOf()
  userEmail.value = userStore.currentUser.email.valueOf()
  totalAmountSaved.value = userStore.currentUser.userStat?.totalSaved?.valueOf() ?? 0
  totalAmountSaved.value = userStore.currentUser.userStat?.totalSaved?.valueOf() ?? 0
  totalSavingGoalsCompleted.value = userStore.currentUser.userStat?.completedGoals?.valueOf() ?? 0
  totalChallengesCompleted.value =
    userStore.currentUser.userStat?.completedChallenges?.valueOf() ?? 0
}

/**
 * Handles logout when logout button inside modal is clicked.
 */
function handleLogout() {
  userStore.logOut()
}

/**
 * Closes log out modal.
 */
function closeModal() {
  showLogoutModal.value = false
}

/**
 * Log out, removes access and refresh token from session storage.
 */
function logOut() {
  showLogoutModal.value = true
}
/**
 * Simple router push to saving goals page.
 */
function pushToSavingGoals() {
  router.push('/savingGoals')
}
</script>
