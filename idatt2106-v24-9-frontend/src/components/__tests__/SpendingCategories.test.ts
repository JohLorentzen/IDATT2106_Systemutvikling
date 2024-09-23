import { mount, flushPromises } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import SpendingCategories from '../SpendingCategories.vue'
import api from '../../services/enumService/index'
describe('SpendingCategories.vue', () => {
  const mockCategories = [
    { name: 'Transport', norwegianText: 'Transport' },
    { name: 'Mat', norwegianText: 'Mat' },
    { name: 'Underholdning', norwegianText: 'Underholdning' }
  ]

  beforeEach(() => {
    vi.spyOn(api, 'getCategories').mockResolvedValue({ data: mockCategories })
  })

  it('Shows the list of spending categories', async () => {
    const wrapper = mount(SpendingCategories)

    await flushPromises()

    const listItems = wrapper.findAll('li')
    expect(listItems).toHaveLength(mockCategories.length)

    expect(listItems[0].text()).toContain('Transport')
    expect(listItems[1].text()).toContain('Mat')
    expect(listItems[2].text()).toContain('Underholdning')
  })

  it('Activates the submit button when three categories are selected', async () => {
    const wrapper = mount(SpendingCategories)

    await flushPromises()

    const button = wrapper.find('button')
    const checkboxes = wrapper.findAll('input[type="checkbox"]')

    expect(button.attributes('disabled'))

    checkboxes[0].setValue(true)
    checkboxes[1].setValue(true)
    checkboxes[2].setValue(true)

    await flushPromises()
  })
})
