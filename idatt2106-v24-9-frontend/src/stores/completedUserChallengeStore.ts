import { ref } from 'vue'
import type { AxiosResponse } from 'axios'
import { defineStore } from 'pinia'
import type { CompletedUserChallenge } from '@/services/savingPath/types'
import api from '@/services/savingPath'

export const useCompletedUserChallengesStore = defineStore('completedUserChallenge', () => {
  const completedUserChallenges = ref<CompletedUserChallenge[]>()

  /**
   * Fetch all the logged-in user's completed challenges and add/store them to 'completedUserChallenges'.
   */
  async function getCompletedUserChallenges() {
    try {
      const response: AxiosResponse<CompletedUserChallenge[]> =
        await api.getCompletedUserChallenges()
      const data = response.data

      if (response.status === 200) {
        completedUserChallenges.value = data
        return completedUserChallenges.value
      } else {
        console.error('Completed user challenges could not be retrieved! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  return {
    completedUserChallenges,
    getCompletedUserChallenges
  }
})
