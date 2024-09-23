import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import LoginForm from '../LoginForm.vue'
import text from '../../assets/text.no.json'

vi.mock('primevue/usetoast', () => ({
  useToast: vi.fn()
}))

vi.mock('@/stores/userStore', () => ({
  useUserStore: vi.fn()
}))

describe('LoginForm.vue', () => {
  it('renders the form correctly', () => {
    const wrapper = mount(LoginForm)

    expect(wrapper.find('form').exists()).toBe(true)
    expect(wrapper.find('input#email-address').exists()).toBe(true)
    expect(wrapper.find('input#password').exists()).toBe(true)
    expect(wrapper.find('button[type="submit"]').exists()).toBe(true)
  })

  it('takes form input', () => {
    const wrapper = mount(LoginForm)

    wrapper.find('input#email-address').setValue('testemail')
    expect((wrapper.find('input#email-address').element as HTMLInputElement).value).toEqual(
      'testemail'
    )

    wrapper.find('input#password').setValue('testpassword')
    expect((wrapper.find('input#password').element as HTMLInputElement).value).toEqual(
      'testpassword'
    )
  })

  it('shows the correct text-labels from the text-JSON', () => {
    const wrapper = mount(LoginForm)

    expect((wrapper.find('label#email-label').element as HTMLLabelElement).textContent).toEqual(
      text.loginEmail
    )

    expect((wrapper.find('label#password-label').element as HTMLLabelElement).textContent).toEqual(
      text.password
    )

    expect((wrapper.find('input#email-address').element as HTMLInputElement).placeholder).toEqual(
      text.loginEmail
    )

    expect((wrapper.find('input#password').element as HTMLInputElement).placeholder).toEqual(
      text.password
    )

    expect(
      (wrapper.find('button[type="submit"]').element as HTMLButtonElement).textContent
    ).toEqual(text.login)

    expect((wrapper.find('#register-link').element as HTMLAnchorElement).innerHTML).toEqual(
      text.register
    )
  })
})
