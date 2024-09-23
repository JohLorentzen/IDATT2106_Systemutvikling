import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils' // Ensure flushPromises is imported
import ProfilePage from '../ProfileComponent.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// Mock nødvendige moduler
vi.mock('@/router', () => ({
  push: vi.fn()
}))
vi.mock('@/stores/userStore', () => ({
  useUserStore: () => {
    return {
      getUserStats: vi.fn().mockResolvedValue({}), // Pass an empty object
      currentUser: {
        fullName: 'John Doe',
        email: 'john@example.com',
        userStat: {
          totalSaved: 500,
          completedGoals: 10,
          completedChallenges: 5
        }
      },
      logOut: vi.fn()
    }
  }
}))

describe('ProfilePage', () => {
  let wrapper: any

  beforeEach(async () => {
    // Make beforeEach async
    wrapper = mount(ProfilePage, {
      global: {
        components: {
          FontAwesomeIcon
        },
        stubs: ['router-link', 'router-view'],
        mocks: {
          $router: { push: vi.fn() },
          text: {
            mySavingGoals: 'My Saving Goals',
            editFinanceProfile: 'Edit Finance Profile',
            logout: 'Logout',
            savingGoalsCompleted: 'Completed Saving Goals',
            totalMoneySaved: 'Total Money Saved',
            challengesCompleted: 'Challenges Completed'
          }
        }
      }
    })
    await flushPromises() // Wait for all promises to resolve
  })

  it('displays the user full name', async () => {
    await wrapper.vm.$nextTick() // Ensure Vue has updated the DOM
    const userNameDisplay = wrapper.find('h2').text()
    expect(userNameDisplay).toContain('John Doe')
  })

  it('displays the user email', async () => {
    await wrapper.vm.$nextTick()
    const userEmailDisplay = wrapper.find('p.text-sm.text-gray-500').text()
    expect(userEmailDisplay).toBe('john@example.com')
  })
})
