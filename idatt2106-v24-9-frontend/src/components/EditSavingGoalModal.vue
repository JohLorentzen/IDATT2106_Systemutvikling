<template>
  <div
    v-show="isModalOpen"
    class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity z-50"
    @click.self="closeModal"
  >
    <div
      class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0"
      @click.stop
    >
      <div
        class="inline-block align-bottom bg-white rounded-xl text-left shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full"
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-headline"
      >
        <div class="flex justify-between items-start p-4">
          <div class="flex-grow">
            <!-- Optional placeholder for a title or nothing -->
          </div>
          <button @click="closeModal" class="text-gray-500 hover:text-gray-700 focus:outline-none">
            <svg
              class="h-6 w-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              ></path>
            </svg>
          </button>
        </div>

        <div class="bg-white rounded-xl mb-4 px-4 pb-4 sm:p-6 sm:pb-4">
          <div class="sm:flex sm:items-start">
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
              <h3 class="text-2xl font-bold text-center text-gray-800 mt-4" id="modal-headline">
                {{ isNewGoal ? text.addNewGoal : text.editGoal }}
              </h3>
              <div style="width: 24px"></div>
              <div class="mt-2">
                <form @submit.prevent="isNewGoal ? createNewGoal() : saveChanges()">
                  <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1 text-left">
                      {{ text.savingGoalTitlePrompt }}
                    </label>
                    <input
                      type="text"
                      class="rounded-md border border-gray-300 w-full px-3 py-2"
                      v-model="localGoal.title"
                    />
                  </div>
                  <div class="mb-4 flex items-center space-x-2 relative">
                    <label class="block text-sm font-medium text-gray-700 mb-1">
                      {{ text.activateGoalPrompt }}
                    </label>
                    <div class="flex items-center relative">
                      <input
                        type="checkbox"
                        class="form-checkbox h-5 w-5 text-blue-600"
                        v-model="localGoal.active"
                      />
                      <button
                        class="ml-2"
                        @mouseover="showTooltip = true"
                        @mouseleave="showTooltip = false"
                        @focus="showTooltip = true"
                        @blur="showTooltip = false"
                      >
                        ?
                      </button>
                      <div
                        v-if="showTooltip"
                        class="absolute bg-gray-200 p-2 rounded shadow text-sm"
                        style="width: 200px; right: -220px"
                      >
                        {{ text.activateStatusExplanation }}
                      </div>
                    </div>
                  </div>
                  <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1 text-left">
                      {{ text.savingGoalEndDatePrompt }}
                    </label>
                    <input
                      type="date"
                      class="rounded-md border border-gray-300 w-full px-3 py-2"
                      v-model="localGoal.endDate"
                      :min="today"
                    />
                  </div>
                  <div class="mb-4 relative">
                    <label class="block text-sm font-medium text-gray-700 mb-1 text-center">
                      {{ text.savingGoalAmountPrompt }}
                    </label>
                    <div class="flex items-center">
                      <input
                        type="text"
                        class="text-center text-2xl border-gray-300 flex-grow"
                        style="width: 4.5rem; padding-right: 0.25rem"
                        v-model="localGoal.goalAmount"
                      />
                      <span class="text-2xl absolute right-0 pr-2">{{ text.currencySymbol }}</span>
                      <div
                        class="absolute bottom-0 left-1/2 transform -translate-x-1/2 border-b-2 border-gray-300"
                        style="width: 5rem"
                      ></div>
                    </div>
                  </div>
                  <div class="px-4 py-3 sm:px-6 sm:flex sm:flex-col sm:flex-row sm:justify-center">
                    <div class="flex justify-center items-center w-full">
                      <p v-if="!isFormValid" class="text-red-600 text-sm">
                        {{ text.fillAllFields }}
                      </p>
                    </div>
                    <div
                      class="flex flex-col items-center justify-center space-y-4 sm:space-y-0 sm:space-x-4 sm:flex-row mt-5"
                    >
                      <button
                        type="submit"
                        :disabled="!isFormValid"
                        class="w-full sm:w-auto bg-primaryBtn rounded-md px-4 py-2 hover:bg-hooverBtn text-white"
                        :class="{ 'opacity-50': !isFormValid }"
                      >
                        {{ isNewGoal ? text.addNewGoal : text.saveGoal }}
                      </button>
                      <button
                        type="button"
                        @click="deleteGoal"
                        class="w-full sm:w-auto bg-white text-red-600 hover:ring-2 hover:ring-red-600 hover:shadow-lg rounded-md px-4 py-2 shadow-md border border-red-600 transition-all duration-200"
                        v-if="!isNewGoal"
                      >
                        {{ text.archiveGoal }}
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import type { PropType } from 'vue'
import text from '@/assets/text.no.json'
import api from '@/services/savingPath/'
import type { SavingsGoal } from '@/services/savingPath/types'

const props = defineProps({
  isModalOpen: Boolean,
  isNewGoal: Boolean,
  goal: {
    type: Object as PropType<SavingsGoal>,
    required: true
  }
})
const isFormValid = computed(() => {
  return localGoal.value.title && localGoal.value.endDate && localGoal.value.goalAmount > 0
})

const emit = defineEmits(['close', 'fetchGoals', 'updateGoal'])
const today = new Date().toISOString().split('T')[0]
const showTooltip = ref(false)
const localGoal = ref({ ...props.goal })

watch(
  () => props.goal,
  (newGoal) => {
    localGoal.value = { ...newGoal }
  },
  { deep: true }
)

/**
 * Saves the changes made to the goal.
 * Posts the updated goal to the API and emits the updated goal to the parent component.
 */
async function saveChanges() {
  try {
    closeModal()
    // Create a new instance of SavingsGoal based on localGoal.value
    const newGoal = { ...localGoal.value }
    // Update the status of the current goal
    await api.postActiveSavingsGoal(newGoal)
    emit('updateGoal', newGoal)
    emit('fetchGoals')
  } catch (error) {
    console.error(error)
  }
}

/**
 * Creates a new goal.
 * Posts the new goal to the API and emits the new goal to the parent component.
 */
async function createNewGoal() {
  closeModal()
  try {
    // Create a new instance of SavingsGoal based on localGoal.value
    const newGoal = { ...localGoal.value }
    await api.postActiveSavingsGoal(newGoal)
    emit('updateGoal', newGoal)
    emit('fetchGoals')
  } catch (error) {
    console.error(error)
  }
}

function closeModal() {
  emit('close')
}

/**
 * Deletes the goal.
 * Deletes the goal from the API and emits the deletion to the parent component.
 */
async function deleteGoal() {
  try {
    await api.deleteSavingsGoal(localGoal.value.id!)
    emit('close')
    emit('fetchGoals')
  } catch (e) {
    console.log('Deletion error: ' + e)
  }
}
</script>
