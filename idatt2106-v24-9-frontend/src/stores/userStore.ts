import { ref } from 'vue'
import type { AxiosResponse } from 'axios'
import { defineStore } from 'pinia'
import type { User, UserInsight, UserStats } from '@/services/userService/types'
import type { Category, Interest, LifeSituation } from '@/enums/enums'
import enumApi from '@/services/enumService'
import router from '@/router'
import api from '@/services/userService/'

export const useUserStore = defineStore('user', () => {
  const defaultUser: User = {
    email: 'default@user.com',
    password: '',
    fullName: 'default fullname',
    phoneNumber: null,
    createdAt: null,
    userInsight: null
  }
  const currentUser = ref<User>(defaultUser)
  const isLoggedIn = ref(false)
  const hasUserInsight = ref<boolean>(false)

  let lifeSituations: LifeSituation[] = []
  let interests: Interest[] = []
  let categories: Category[] = []

  /**
   * Fetch all enums currently stored in the database and add them to their respective arrays.
   */
  async function fetchEnums() {
    const lifeSituationResponse: AxiosResponse<LifeSituation[]> = await enumApi.getLifeSituations()
    const interestResponse: AxiosResponse<Interest[]> = await enumApi.getInterests()
    const categoryResponse: AxiosResponse<Category[]> = await enumApi.getCategories()

    if (lifeSituationResponse.status === 200) lifeSituations = lifeSituationResponse.data
    if (interestResponse.status === 200) interests = interestResponse.data
    if (categoryResponse.status === 200) categories = categoryResponse.data
  }

  /**
   * Add/store a user to the store and fetch all enums currently stored in the database.
   * @param user The user to save in the store.
   */
  async function saveUserInStore(user: User) {
    if (user.email) currentUser.value.email = user.email
    if (user.fullName) currentUser.value.fullName = user.fullName
    if (user.phoneNumber) currentUser.value.phoneNumber = user.phoneNumber
    if (user.createdAt) currentUser.value.createdAt = user.createdAt
    fetchEnums()
    // if (user.userInsight) currentUser.value.userInsight = user.userInsight
  }

  /**
   * Assign a user insight to the logged-in user.
   * @param userInsight The user insight to add to the logged-in user.
   */
  async function saveUserInsightToUser(userInsight: UserInsight) {
    currentUser.value.userInsight = userInsight
  }
  async function logOut() {
    sessionStorage.removeItem('accessToken')
    sessionStorage.removeItem('refreshToken')
    router.push('/')
  }

  /**
   * Get user stats for a user.
   */
  async function getUserStats() {
    const response: AxiosResponse<UserStats> = await api.getUserStats()
    if (response.status === 200) {
      currentUser.value.userStat = response.data
    } else {
      console.log('Failed fetching userStats: ' + response.status)
    }
  }
  return {
    currentUser,
    lifeSituations,
    interests,
    categories,
    isLoggedIn,
    hasUserInsight,
    saveUserInStore,
    saveUserInsightToUser,
    getUserStats,
    logOut
  }
})

// interface userInsight {
//     id : number;
//     lifeSituation: string;
//     interests: string[];
//     categories: string[];

// }

// interface UserState {
//     fullName: string;
//     password: string;
//     email: string;
//     createdAt: Date;
//     userInsight : userInsight;
// }

// export const useUserStore = defineStore({
//     id: 'user',
//     state: () => ({
//         fullName: 'John Doe',
//         password: 'password',
//         email: 'JohnDoe@gmail.com',
//         createdAt: new Date(4-20-2069),
//         userInsight: {
//             id: 1,
//             lifeSituation: 'STUDENT',
//             interests: ['STOCKS'],
//             categories: ['SHOPPING', 'GROCERIES']
//         }
//     }) as UserState,
//     getters: {
//         getFullName: state => state.fullName,
//         getPassword: state => state.password,
//         getEmail: state => state.email,
//         getCreatedAt: state => state.createdAt,
//         getUserInsight: state => state.userInsight,

//         getLifeSituation: state => state.userInsight.lifeSituation,
//         getInterests: state => state.userInsight.interests,
//         getCategories: state => state.userInsight.categories,
//     },
//     actions: {
//         setFullName(fullName: string) {
//             this.fullName = fullName;
//         },
//         setPassword(password: string) {
//             this.password = password;

//         },
//         setEmail(email: string) {
//             this.email = email;
//         },
//         setCreatedAt(createdAt: Date) {
//             this.createdAt = createdAt;
//         },
//         setUserInsight(userInsight: userInsight) {
//             this.userInsight = userInsight;
//         },
//         setLifeSituation(lifeSituation: string) {
//             this.userInsight.lifeSituation = lifeSituation;
//         },
//         setInterests(interests: string[]) {
//             this.userInsight.interests = interests;
//         },
//         setCategories(categories: string[]) {
//             this.userInsight.categories = categories;
//         }
//     }
// });
