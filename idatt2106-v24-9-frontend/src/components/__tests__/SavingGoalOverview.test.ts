import { mount } from '@vue/test-utils'
import SavingGoalOverview from '../SavingGoalOverview.vue'
import { describe, it, expect } from 'vitest'

describe('SavingGoalOverview.vue', () => {
  it('renders goal title when passed in as prop', () => {
    const goal = {
      title: 'Test Goal',
      endDate: '',
      goalAmount: 0,
      active: false
    }
    const wrapper = mount(SavingGoalOverview, {
      props: {
        goal: goal,
        isCompleted: false
      }
    })

    expect(wrapper.text()).toContain('Test Goal')
  })

  it('emits openModal event when edit button is clicked', async () => {
    const goal = {
      title: 'Test Goal',
      endDate: '',
      goalAmount: 0,
      active: false
    }
    const wrapper = mount(SavingGoalOverview, {
      props: {
        goal: goal,
        isCompleted: false
      }
    })

    await wrapper.find('button').trigger('click')
    expect(wrapper.emitted()).toHaveProperty('openModal')
  })
  it('renders goalAmount and savedAmount when passed in as prop', () => {
    const goal = {
      title: 'Test Goal',
      endDate: '',
      goalAmount: 1000,
      savedAmount: 500,
      active: false
    }
    const wrapper = mount(SavingGoalOverview, {
      props: {
        goal: goal,
        isCompleted: false
      }
    })

    expect(wrapper.props().goal).toEqual(goal)
    expect(wrapper.props().isCompleted).toBe(false)

    expect(wrapper.props().goal.goalAmount).toEqual(1000)
    expect(wrapper.props().goal.savedAmount).toEqual(500)
  })

  it('renders goalReached text when isCompleted prop is true and goal.isTargetReached is true', () => {
    const goal = {
      title: 'Test Goal',
      endDate: '',
      goalAmount: 0,
      isTargetReached: true,
      active: false
    }
    const wrapper = mount(SavingGoalOverview, {
      props: {
        goal: goal,
        isCompleted: true
      }
    })

    expect(wrapper.text()).toContain('Sparemål oppnådd!')
  })
})
