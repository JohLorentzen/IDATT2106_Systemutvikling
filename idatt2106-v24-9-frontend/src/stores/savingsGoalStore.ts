import { ref } from 'vue'
import type { AxiosResponse } from 'axios'
import { defineStore } from 'pinia'
import type { SavingsGoal } from '@/services/savingPath/types'
import savingsGoalApi from '@/services/savingPath'

export const useSavingsGoalStore = defineStore('savingsGoal', () => {
  const defaultSavingsGoal: SavingsGoal = {
    id: 1,
    title: 'default title',
    goalAmount: 0,
    savedAmount: 0,
    endDate: new Date(),
    daysLeft: 0,
    active: true,
    isTargetReached: false
  }

  const activeSavingsGoal = ref<SavingsGoal>(defaultSavingsGoal)
  const savingsGoals = ref<SavingsGoal[]>()

  /**
   * Fetch the logged in user's active savings goal and add/store it to 'activeSavingsGoal'.
   */
  async function getActiveSavingsGoal() {
    try {
      const response: AxiosResponse<SavingsGoal> = await savingsGoalApi.getActiveSavingsGoal()
      const data = response.data

      if (response.status === 200) {
        activeSavingsGoal.value = data
      } else {
        console.error('The active savings goal could not be retrieved! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  /**
   * Post/save/update a savings goal.
   * @param savingsGoal The savings goal to post/save.
   */
  async function postActiveSavingsGoal(savingsGoal: SavingsGoal) {
    try {
      const response: AxiosResponse<SavingsGoal> =
        await savingsGoalApi.postActiveSavingsGoal(savingsGoal)
      if (response.status === 200) {
        return
      } else {
        console.error('The active savings goal could not be posted! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  return {
    activeSavingsGoal,
    savingsGoals,
    getActiveSavingsGoal,
    postActiveSavingsGoal
  }
})
