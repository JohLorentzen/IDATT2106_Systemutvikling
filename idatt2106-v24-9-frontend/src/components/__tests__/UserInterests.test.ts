import { mount, flushPromises } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import UserInterests from '../UserInterests.vue'
import api from '../../services/enumService/index'

describe('UserInterests.vue', () => {
  const mockInterests = [
    { name: 'DEBT', norwegianText: 'Gjeld' },
    { name: 'STOCKS', norwegianText: 'Aksjer' },
    { name: 'CRYPTOCURRENCY', norwegianText: 'Kryptovaluta' }
  ]

  beforeEach(() => {
    vi.spyOn(api, 'getInterests').mockResolvedValue({ data: mockInterests })
  })

  it('Shows the list of the interest alternatives', async () => {
    const wrapper = mount(UserInterests)

    await flushPromises()

    const listItems = wrapper.findAll('li')
    expect(listItems).toHaveLength(mockInterests.length)

    expect(listItems[0].text()).toContain('Gjeld')
    expect(listItems[1].text()).toContain('Aksjer')
    expect(listItems[2].text()).toContain('Kryptovaluta')
  })

  it('Activates the next button when three intersts is selected', async () => {
    const wrapper = mount(UserInterests)

    await flushPromises()

    const button = wrapper.find('button')
    const checkboxes = wrapper.findAll('input[type="checkbox"]')

    expect(button.attributes('disabled'))

    checkboxes[0].setValue(true)
    checkboxes[1].setValue(true)
    checkboxes[2].setValue(true)

    await flushPromises()

    expect(button.attributes('disabled'))
  })
})
