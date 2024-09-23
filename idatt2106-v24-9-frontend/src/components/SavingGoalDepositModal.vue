<template>
  <div
    class="bg-gray-400 fixed flex justify-center items-center inset-0 bg-opacity-75 z-50"
    @click="closeModal"
  >
    <div class="bg-white flex flex-col sm:p-8 p-4 rounded relative" @click.stop>
      <FontAwesomeIcon
        icon="xmark"
        class="fa-xl absolute top-4 right-4 text-gray-500 hover:text-gray-800 cursor-pointer"
        @click="closeModal"
      />
      <h1 class="text-3xl font-bold text-gray-800">{{ text.savingGoalDepositModalTitle }}</h1>
      <p>{{ text.savingGoalDepositModalSubtitle }}</p>
      <div class="flex-col gap-4 mt-4">
        <label class="text-gray-500 text-sm">{{ text.savingGoalDepositModalLabel }}</label>
        <input
          v-model="depositAmount"
          type="number"
          :placeholder="text.savingGoalDepositModalInputPH"
          class="rounded-md border border-gray-300 w-full px-3 py-2"
          :class="{ 'border-red-600': isInvalidDeposit }"
        />
      </div>
      <p v-if="isInvalidDeposit" class="text-red-600 text-sm mt-1">
        {{ text.savingGoalDepositModalError }}
      </p>
      <div class="flex flex-col gap-1 mt-4">
        <button
          class="w-full sm:w-auto bg-green-500 text-white rounded-md px-4 py-2 hover:bg-green-600"
          @click="handleDeposit"
          id="deposit"
        >
          {{ text.savingGoalDepositModalDepositBtn }}
        </button>
        <button
          class="w-full sm:w-auto bg-white text-red-600 hover:shadow-lg rounded-md px-4 py-2 shadow-md border border-red-600 transition-all duration-200 hover:font-bold"
          @click="closeModal"
        >
          {{ text.savingGoalDepositModalCloseBtn }}
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { ref } from 'vue'
import text from '@/assets/text.no.json'

const depositAmount = ref<number>(0)
const isInvalidDeposit = ref<boolean>(false)

const emit = defineEmits(['close', 'makeDeposit'])

function closeModal() {
  emit('close')
}

/**
 * Checks if input field is empty, or if amount is negative and updates style variable.
 * If inputted amount passes checks, it will emit the amount and close the modal.
 */
function handleDeposit() {
  if (depositAmount.value == null || depositAmount.value <= 0) {
    isInvalidDeposit.value = true
    return
  }
  emit('makeDeposit', depositAmount.value)
  emit('close')
}
</script>
