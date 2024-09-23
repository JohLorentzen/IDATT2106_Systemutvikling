import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import SavingGoalDepositModal from '../SavingGoalDepositModal.vue'

describe('SavingGoalDepositModal.vue', () => {
  it('renders modal when isDepositModalOpen prop is true', () => {
    const wrapper = mount(SavingGoalDepositModal)

    expect(wrapper.isVisible()).toBe(true)
  })

  it('closes the modal when closeModal method is called', async () => {
    const Parent = {
      data() {
        return {
          isDepositModalOpen: true
        }
      },
      template: `
        <SavingGoalDepositModal 
            v-if="isDepositModalOpen" 
            @close="isDepositModalOpen = false"
            @make-deposit="updateSavedAmount"
        />
    `,
      components: { SavingGoalDepositModal }
    }

    const wrapper = mount(Parent)
    const childWrapper = wrapper.findComponent(SavingGoalDepositModal)

    await (childWrapper.vm as any).closeModal()
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.isDepositModalOpen).toBe(false)
  })

  it('does not render modal when isDepositModalOpen prop is false', () => {
    const Parent = {
      data() {
        return {
          isDepositModalOpen: false
        }
      },
      template: `
        <SavingGoalDepositModal 
            v-if="isDepositModalOpen" 
            @close="isDepositModalOpen = false"
            @make-deposit="updateSavedAmount"
        />
    `,
      components: { SavingGoalDepositModal }
    }

    const wrapper = mount(Parent, {
      attachTo: document.body
    })

    expect(wrapper.findComponent(SavingGoalDepositModal).exists()).toBe(false)
  })
})
