import { mount } from '@vue/test-utils'
import EditSavingGoalModal from '../EditSavingGoalModal.vue'
import { describe, it, expect } from 'vitest'

describe('EditSavingGoalModal.vue', () => {
  it('renders modal when isModalOpen prop is true', async () => {
    const wrapper = mount(EditSavingGoalModal, {
      props: {
        isModalOpen: true,
        isNewGoal: true,
        goal: {
          title: '',
          endDate: '',
          goalAmount: 0,
          active: false
        }
      }
    })

    expect(wrapper.isVisible()).toBe(true)
  })

  it('closes the modal when closeModal method is called', async () => {
    const Parent = {
      data() {
        return {
          isModalOpen: true
        }
      },
      template: `
      <EditSavingGoalModal
        :is-modal-open="isModalOpen"
        :is-new-goal="true"
        :goal="{ title: '', endDate: '', goalAmount: 0, active: false }"
        @close="isModalOpen = false"
      />
    `,
      components: { EditSavingGoalModal }
    }

    const wrapper = mount(Parent)
    const childWrapper = wrapper.findComponent(EditSavingGoalModal)

    await (childWrapper.vm as any).closeModal()
    await wrapper.vm.$nextTick() // Wait for DOM update

    // Check if the isModalOpen data property of the parent component has been updated to false
    expect(wrapper.vm.isModalOpen).toBe(false)
  })

  it('does not render modal when isModalOpen prop is false', async () => {
    const wrapper = mount(EditSavingGoalModal, {
      props: {
        isModalOpen: false,
        isNewGoal: true,
        goal: {
          title: '',
          endDate: '',
          goalAmount: 0,
          active: false
        }
      }
    })

    expect(wrapper.isVisible()).toBe(false)
  })
})
