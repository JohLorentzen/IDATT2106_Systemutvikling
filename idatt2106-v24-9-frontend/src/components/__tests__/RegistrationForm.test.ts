import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import RegistrationForm from '../RegistrationForm.vue'
import text from '../../assets/text.no.json'

vi.mock('primevue/usetoast', () => ({
  useToast: vi.fn()
}))

describe('RegistrationForm.vue', () => {
  it('renders the form correctly', () => {
    const wrapper = mount(RegistrationForm)
    expect(wrapper.find('form').exists()).toBe(true)
    expect(wrapper.find('input#name').exists()).toBe(true)
    expect(wrapper.find('input#email').exists()).toBe(true)
    expect(wrapper.find('input#mobile').exists()).toBe(true)
    expect(wrapper.find('input#password').exists()).toBe(true)
    expect(wrapper.find('input#confirm-password').exists()).toBe(true)
    expect(wrapper.find('button[type="submit"]').exists()).toBe(true)
  })

  it('takes form input', () => {
    const wrapper = mount(RegistrationForm)

    wrapper.find('input#name').setValue('testname')
    expect((wrapper.find('input#name').element as HTMLInputElement).value).toEqual('testname')

    wrapper.find('input#email').setValue('testemail')
    expect((wrapper.find('input#email').element as HTMLInputElement).value).toEqual('testemail')

    wrapper.find('input#mobile').setValue('testmobile')
    expect((wrapper.find('input#mobile').element as HTMLInputElement).value).toEqual('testmobile')

    wrapper.find('input#password').setValue('testpassword')
    expect((wrapper.find('input#password').element as HTMLInputElement).value).toEqual(
      'testpassword'
    )

    wrapper.find('input#confirm-password').setValue('testconfirmpassword')
    expect((wrapper.find('input#confirm-password').element as HTMLInputElement).value).toEqual(
      'testconfirmpassword'
    )
  })

  it('shows the correct text-labels from the text-JSON', () => {
    const wrapper = mount(RegistrationForm)

    expect((wrapper.find('label#name-label').element as HTMLLabelElement).textContent).toEqual(
      text.name
    )

    expect((wrapper.find('label#email-label').element as HTMLLabelElement).textContent).toEqual(
      text.email
    )

    expect((wrapper.find('label#mobile-label').element as HTMLInputElement).textContent).toEqual(
      text.phoneNumber
    )

    expect((wrapper.find('label#password-label').element as HTMLInputElement).textContent).toEqual(
      text.password
    )

    expect(
      (wrapper.find('label#confirm-password-label').element as HTMLInputElement).textContent
    ).toEqual(text.ConfirmPassword)

    expect(
      (wrapper.find('button[type="submit"]').element as HTMLButtonElement).textContent
    ).toEqual(text.nextBtn)
  })
})
