import { ref } from 'vue'
import type { AxiosResponse } from 'axios'
import { defineStore } from 'pinia'
import type { UserChallenge } from '@/services/savingPath/types'
import api from '@/services/savingPath'

export const useUserChallengesStore = defineStore('userChallenge', () => {
  const userChallenges = ref<UserChallenge[]>()

  /**
   * Fetch all the logged-in user's challenges and add/store them to 'userChallenges'.
   */
  async function getUserChallenges() {
    try {
      const response: AxiosResponse = await api.getUserChallenges()

      if (response.status === 200) {
        userChallenges.value = response.data.content
        return userChallenges.value
      } else {
        console.error('User challenges could not be retrieved! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }

  /**
   * Post/save(/update) a user challenge.
   * @param userChallenge The user challenge to post/save(/update).
   */
  async function postUserChallenge(userChallenge: UserChallenge) {
    try {
      const response: AxiosResponse = await api.postUserChallenge(userChallenge)

      if (response.status === 200) {
        return
      } else {
        console.error('The user challenge could not be posted! Please try again.')
      }
    } catch (error) {
      console.error(error)
    }
  }
  async function acceptChallenge(userChallenge: UserChallenge) {
    try {
      const response: AxiosResponse = await api.acceptChallenge(userChallenge)
      if (response.status === 200) {
        return
      }
    } catch (error) {
      console.error(error)
    }
  }

  return {
    acceptChallenge,
    userChallenges,
    getUserChallenges,
    postUserChallenge
  }
})

// export const useUserChallengesStore = defineStore({
//   id: 'userChallenges',
//   state: () => ({

//   }),
//   actions: {
//     addUserChallenge(userChallenge: UserChallenge) {
//       this.userChallenges.push(userChallenge)
//     },
//     removeUserChallenge(id: number) {
//       const idx = this.userChallenges.findIndex((userChallenge) => userChallenge.id === id)
//       if (idx === -1) {
//         return
//       }
//       this.userChallenges.splice(idx, 1)
//     },
//     updateUserChallenge(id: number, updatedUserChallenge: UserChallenge) {
//       const idx = this.userChallenges.findIndex((userChallenge) => userChallenge.id === id)
//       if (idx === -1) {
//         return
//       }
//       this.userChallenges[idx] = updatedUserChallenge
//     }
//   }
// })
