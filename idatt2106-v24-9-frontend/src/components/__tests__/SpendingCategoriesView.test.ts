import { mount, flushPromises } from '@vue/test-utils'
import SpendingCategoriesView from '../../views/SpendingCategoriesView.vue'
import { describe, beforeEach, vi, it, expect } from 'vitest'
import axios from 'axios'

// Correct mocking pattern for axios
const mockCategories = ['Food', 'Transport', 'Entertainment']

describe('SpendingCategoriesView.vue', () => {
  let wrapper

  beforeEach(async () => {
    vi.resetAllMocks()

    vi.spyOn(axios, 'get').mockResolvedValue({ data: mockCategories })

    wrapper = mount(SpendingCategoriesView, {
      global: {}
    })

    await flushPromises()
  })

  it('executes goToNext without error when the submit button is clicked with three selected categories', async () => {
    const checkboxes = wrapper.findAll('input[type="checkbox"]')

    checkboxes.slice(0, 3).forEach((checkbox) => {
      checkbox.setChecked(true)
    })

    await wrapper.vm.$nextTick()

    const submitButton = wrapper.find('button')

    await submitButton.trigger('click')

    expect(wrapper.emitted().error).toBeUndefined()
  })
})
