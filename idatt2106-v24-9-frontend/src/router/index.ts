import { createRouter, createWebHistory } from 'vue-router'
import { ref } from 'vue'
import HomeView from '@/views/HomeView.vue'
import { useUserStore } from '@/stores/userStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/registration',
      name: 'registration',
      component: () => import('@/views/RegistrationView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/ProfileView.vue')
    },
    {
      path: '/savingGoals',
      name: 'savingGoals',
      component: () => import('@/views/SavingGoalsView.vue')
    },
    {
      path: '/userSurvey',
      name: 'userSurvey',
      component: () => import('@/views/UserSurveyView.vue')
    },
    {
      path: '/spendingCategories',
      name: 'spendingCategories',
      component: () => import('@/views/SpendingCategoriesView.vue')
    },
    {
      path: '/challenges',
      name: 'challenges',
      component: () => import('@/views/ChallengeView.vue')
    },
    {
      path: '/budget',
      name: 'budget',
      component: () => import('@/views/BudgetView.vue')
    },
    {
      path: '/explore',
      name: 'explore',
      component: () => import('@/views/ExploreView.vue')
    }
  ],
  linkActiveClass: 'activePage'
})
/**
 * This function is called before each route change.
 * It checks if the user is logged in and if not, redirects to the login page.
 */
router.beforeEach((to, from, next) => {
  const loggedIn = ref(
    sessionStorage.getItem('accessToken') != null && sessionStorage.getItem('refreshToken') != null
  )

  const userStore = useUserStore()
  userStore.isLoggedIn = loggedIn.value

  const openRoutes: string[] = ['login', 'registration']
  if (!loggedIn.value && !openRoutes.includes(<string>to.name)) {
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router
