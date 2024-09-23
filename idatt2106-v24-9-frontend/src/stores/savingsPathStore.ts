import { ref } from 'vue'
import type { AxiosResponse } from 'axios'
import { defineStore } from 'pinia'
import type {
  SavingsPath,
  CompletedUserChallenge,
  UserChallenge
} from '@/services/savingPath/types'
import savingsPathApi from '@/services/savingPath'

export const useSavingsPathStore = defineStore('savingsPath', () => {
  const defaultSavingsPath: SavingsPath = {
    id: null,
    savingsGoal: {
      id: null,
      title: 'default title',
      goalAmount: 0,
      savedAmount: 0,
      endDate: new Date(),
      daysLeft: 7,
      active: true,
      isTargetReached: false
    },
    completedChallenges: [
      {
        id: null,
        userChallenge: {
          id: null,
          goalAmount: 0,
          savedAmount: 0,
          fromDate: new Date(),
          toDate: new Date(Date.now() + 1000 * 3600 * 24),
          challenge: {
            id: null,
            name: 'default name',
            description: 'default description',
            category: 'SHOPPING',
            duration: 1,
            sum: 100,
            averageAmount: 100,
            lifeSituation: 'WORKING',
            tip: 'default tip'
          }
        },
        pointOfCompletion: 50
      }
    ]
  }

  const activeSavingsPath = ref<SavingsPath>(defaultSavingsPath)
  const completedUserChallenges = ref<CompletedUserChallenge[]>()

  async function getActiveSavingsPath() {
    try {
      const response: AxiosResponse<SavingsPath> = await savingsPathApi.getSavingsPath()
      const data = response.data

      if (response.status === 200) {
        activeSavingsPath.value = data
        return data
      } else {
        console.error('The savings path could not be retrieved! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  async function postCompletedUserChallenge(userChallenge: UserChallenge) {
    try {
      const response: AxiosResponse<CompletedUserChallenge> =
        await savingsPathApi.postCompletedUserChallenge(userChallenge)

      if (response.status === 200) {
        return
      } else {
        console.error(
          'The user challenge could not be posted and marked as completed! Please try again.'
        )
      }
    } catch (error) {
      console.error(error)
    }
  }

  async function getCompletedUserChallenges() {
    try {
      const response: AxiosResponse<CompletedUserChallenge[]> =
        await savingsPathApi.getCompletedUserChallenges()

      const data = response.data

      if (response.status === 200) {
        completedUserChallenges.value = data
        return data
      } else {
        console.error('The completed user challenges could not be retrieved! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  return {
    getActiveSavingsPath,
    postCompletedUserChallenge,
    getCompletedUserChallenges
  }
})
