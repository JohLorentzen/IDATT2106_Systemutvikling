<template>
  <div class="flex flex-col w-full items-center justify-center mt-4 rounded-lg">
    <div class="relative p-4 bg-white max-w-4xl rounded-lg shadow-md w-full h-full">
      <slot name="header"></slot>
      <button v-if="!isCompleted" class="absolute top-3 right-4 rounded" @click="openModal">
        <FontAwesomeIcon
          :icon="['fas', 'pen-to-square']"
          class="text-primaryBtn hover:text-hooverBtn text-2xl ml-4"
        />
      </button>
      <div v-if="goal.isTargetReached && isCompleted" class="absolute top-0 right-0 m-4 p-2">
        <FontAwesomeIcon icon="award" style="color: #fdc708; font-size: 2em" />
      </div>
      <div v-else-if="isCompleted" class="absolute top-0 right-0 m-4 p-2">
        <FontAwesomeIcon :icon="['fas', 'sack-xmark']" style="color: #ff0000; font-size: 2em" />
      </div>
      <h3 class="text-lg font-semibold text-gray-700">{{ goal.title }}</h3>
      <p class="text-gray-600">
        {{ isCompleted ? formatDate(goal.dateAchieved) : `${goal.daysLeft} ${text.daysLeft}` }}
      </p>
      <div class="flex items-center justify-between mt-2">
        <span class="text-sm font-medium text-gray-500">
          <strong class="text-black"
            >{{ text.currency }} {{ formatNumber(goal.savedAmount) }}</strong
          >
          / {{ text.currency }} {{ formatNumber(goal.goalAmount) }}
        </span>
        <!-- Handlinger basert på side og målstatus -->
        <RouterLink
          v-if="isGoalOverviewPage && !isCompleted"
          to="/"
          class="px-4 py-2 text-primary-text text-sm rounded transition duration-150 ease-in-out flex items-center space-x-2 hover:underline"
        >
          <span>
            {{ text.toSavingsPath }}
          </span>
          <font-awesome-icon icon="arrow-right" class="text-2xl" />
        </RouterLink>

        <button
          v-if="!isGoalOverviewPage && !isCompleted"
          class="px-4 py-2 bg-green-500 text-white text-sm rounded hover:bg-green-600"
          @click="openDepositModal"
        >
          {{ text.makeDeposit }}
        </button>
        <span
          v-if="isCompleted"
          :class="{ 'text-green-500': goal.isTargetReached, 'text-red-500': !goal.isTargetReached }"
          class="text-sm font-medium"
        >
          {{ goal.isTargetReached ? text.goalReached : text.goalNotReached }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { PropType } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import text from '@/assets/text.no.json'
import type { SavingsGoal } from '@/services/savingPath/types'
import { formatNumber } from '@/util/NumberFormatter'

const props = defineProps({
  goal: {
    type: Object as PropType<SavingsGoal>,
    required: true
  },
  isCompleted: Boolean,
  isGoalOverviewPage: Boolean
})
const emit = defineEmits(['openModal', 'openDepositModal'])

const openModal = () => {
  emit('openModal', props.goal)
}

function openDepositModal() {
  emit('openDepositModal')
}

/**
 * Formats date to a readable format.
 * @param date - The date to format.
 */
const formatDate = (date: Date | undefined): string => {
  if (!date) {
    return 'Dato utløpt'
  }
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  }
  return date.toLocaleDateString('no-NO', options)
}
</script>
