import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ChallengeCard from '../ChallengeCard.vue'
import type { UserChallenge, Challenge } from '../../services/savingPath/types'

describe('ChallengeCard.vue', () => {
  it('renders the challenge name and amounts', () => {
    const challenge: Challenge = {
      id: 1,
      name: 'default name',
      description: 'default description',
      category: 'SHOPPING',
      duration: 1,
      sum: 100,
      averageAmount: 100,
      lifeSituation: 'WORKING',
      tip: 'default tip'
    }

    const userChallenge: UserChallenge = {
      id: 1,
      goalAmount: 0,
      savedAmount: 0,
      fromDate: new Date(),
      toDate: new Date(Date.now() + 1000 * 3600 * 24), // tomorrow
      challenge: challenge
    }

    const wrapper = mount(ChallengeCard, {
      props: {
        userChallenge: userChallenge
      }
    })

    expect(wrapper.text()).toContain(userChallenge.challenge.name)
    expect(wrapper.text()).toContain(userChallenge.savedAmount.toString())
    expect(wrapper.text()).toContain(userChallenge.goalAmount.toString())
  })
})
