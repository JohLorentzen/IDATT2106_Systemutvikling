import { describe, test, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import UserIntroduction from '../UserIntroduction.vue'

describe('UserIntroduction.vue', () => {
  test('renders with correct initial state', () => {
    const wrapper = mount(UserIntroduction)

    expect(wrapper.text())

    const slider = wrapper.find('input[type="range"]')
    expect((slider.element as HTMLInputElement).value).toBe('0')

    expect(wrapper.text()).toContain('Nybegynner')
  })

  test('updates skill level when slider is changed', async () => {
    const wrapper = mount(UserIntroduction)

    const slider = wrapper.find('input[type="range"]')
    await slider.setValue('2')
    expect(wrapper.text()).toContain('Gjennomsnittlig')
  })

  test('activates submit button when valid skill level is set', async () => {
    const wrapper = mount(UserIntroduction)

    const submitButton = wrapper.find('button[type="submit"]')
    const slider = wrapper.find('input[type="range"]')

    expect((submitButton.element as HTMLButtonElement).disabled)

    await slider.setValue('3')
    expect((submitButton.element as HTMLButtonElement).disabled)
  })

  test('moves to next step on valid submit', async () => {
    const wrapper = mount(UserIntroduction)

    const submitButton = wrapper.find('button[type="submit"]')
    const slider = wrapper.find('input[type="range"]')

    await slider.setValue('3')
    await submitButton.trigger('click')

    expect(wrapper.vm.currentStep)
  })
})
