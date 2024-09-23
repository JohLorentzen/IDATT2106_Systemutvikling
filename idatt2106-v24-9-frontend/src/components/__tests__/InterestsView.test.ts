import { mount, flushPromises } from '@vue/test-utils'
import InterestsView from '../../views/InterestsView.vue'
import { describe, beforeEach, vi, it } from 'vitest'
import axios from 'axios'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [{ path: '/spendingCategories', component: { template: '<div></div>' } }]
})

const mockInterests = [
  'Aksjer',
  'Investering',
  'Obligasjoner',
  'Gjeld',
  'Eiendom',
  'Kryptovaluta',
  'Personlig økonomi',
  'Pensjonering'
]

describe('InterestsView.vue', () => {
  let wrapper

  beforeEach(async () => {
    vi.spyOn(axios, 'get').mockResolvedValue({ data: mockInterests })

    wrapper = mount(InterestsView, {
      global: { plugins: [router] }
    })

    await flushPromises()
  })

  it('executes goToNext without error when the submit button is clicked', async () => {
    const checkboxes = wrapper.findAll('input[type="checkbox"]')

    checkboxes.slice(0, 3).forEach((checkbox) => checkbox.setChecked(true))

    await wrapper.vm.$nextTick()
    const submitButton = wrapper.find('button')

    await submitButton.trigger('click')
  })
})
