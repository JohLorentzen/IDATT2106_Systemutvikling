import { ref } from 'vue'
import type { AxiosResponse } from 'axios'
import { defineStore } from 'pinia'
import type { Challenge } from '@/services/savingPath/types'
import challengeApi from '@/services/savingPath'

export const useChallengesStore = defineStore('challenge', () => {
  const defaultChallenge: Challenge = {
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

  // const challenge = ref<Challenge>(defaultChallenge)
  const challenges = ref<Challenge[]>()

  /**
   * Fetch a (new) challenge and add/store it to the challenges-array.
   */
  async function getChallenge() {
    try {
      const response: AxiosResponse<Challenge> = await challengeApi.getChallenge()
      const data = response.data

      if (response.status === 200) {
        challenges.value?.push(data)
      } else {
        console.error('Challenge could not be retrieved! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  /**
   * Post/save a challenge.
   * @param challenge The challenge to post/save.
   */
  async function postChallenge(challenge: Challenge) {
    try {
      const response: AxiosResponse = await challengeApi.postChallenge(challenge)
      if (response.status === 200) {
        return
      } else {
        console.error('The challenge could not be posted! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  return {
    defaultChallenge,
    challenges,
    getChallenge,
    postChallenge
  }
})
