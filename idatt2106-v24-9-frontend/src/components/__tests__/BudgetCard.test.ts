import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import BudgetCard from '../BudgetCard.vue'
import type { Category } from '../../enums/enums'
import type { BudgetPostDTO } from '../../services/bugdetService/types'

describe('BudgetCard.vue', () => {
  it('renders the budget post title and amounts', () => {
    const budgetPost: BudgetPostDTO = {
      id: 1,
      budgetedSum: 1000,
      actualSum: 500,
      category: 'TRANSPORTATION'
    }

    const category: Category = {
      name: 'TRANSPORTATION',
      norwegianText: 'TRANSPORT',
      englishText: 'TRANSPORTATION'
    }

    const wrapper = mount(BudgetCard, {
      props: {
        category: category,
        budgetPost: budgetPost
      }
    })

    expect(wrapper.props().budgetPost).toEqual(budgetPost)
    expect(wrapper.props().budgetPost.budgetedSum).toEqual(budgetPost.budgetedSum)
    expect(wrapper.props().budgetPost.actualSum).toEqual(budgetPost.actualSum)
    expect(wrapper.text()).toContain(category.norwegianText)
  })

  it('emits selectCard event when clicked', async () => {
    const budgetPost: BudgetPostDTO = {
      id: 1,
      budgetedSum: 1000,
      actualSum: 500,
      category: 'TRANSPORTATION'
    }

    const category: Category = {
      name: 'TRANSPORTATION',
      norwegianText: 'TRANSPORT',
      englishText: 'TRANSPORTATION'
    }

    const wrapper = mount(BudgetCard, {
      props: {
        category: category,
        budgetPost: budgetPost
      }
    })

    await wrapper.trigger('click')

    expect(wrapper.emitted()).toHaveProperty('selectCard')
    expect(wrapper.emitted().selectCard[0]).toEqual([budgetPost])
  })
})
