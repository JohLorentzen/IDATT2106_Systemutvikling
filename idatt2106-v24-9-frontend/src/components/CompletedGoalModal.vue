<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal flex flex-col" @click.stop>
      <div class="modal-content flex flex-col">
        <h2 class="text-3xl font-bold text-gray-800">{{ modalTitle }}</h2>
        <p class="mt-4">{{ summaryPart1 }}{{ savedAmount }}{{ summaryPart2 }}</p>
        <p>{{ feedback }}</p>
      </div>
      <RouterLink
        to="/savingGoals"
        class="rounded-md w-full py-3 px-4 bg-primaryBtn hover:bg-hooverBtn text-white mt-4 text-center"
        @click="closeModal"
      >
        {{ t.setNewActiveGoalBtnText }}
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import t from '@/assets/text.no.json'
import confetti from 'canvas-confetti'
import { RouterLink } from 'vue-router'

const emits = defineEmits(['close'])

const props = defineProps({
  isReached: Boolean,
  savedAmount: Number
})

const summaryPart1 = t.completedGoalModalFeedbackP1
const summaryPart2 = t.completedGoalModalFeedbackP2

function closeModal() {
  emits('close')
}

/**
 * Computed modal title based on if the goal is reached or not.
 */
const modalTitle = computed(() => {
  if (props.isReached) {
    return t.completedGoalReached
  }
  return t.completedGoalNotReached
})

/**
 * Checks if the goal is reached and returns feedback accordingly.
 */
const feedback = computed(() => {
  if (props.isReached) {
    launchConfetti()
    return t.goalReachedFeedback
  }
  return t.goalNotReachedFeedback
})

/**
 * Lunches confetti when modal is opened.
 */
function launchConfetti() {
  confetti({
    particleCount: 100,
    spread: 70,
    origin: { y: 0.6 }
  })
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: default;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 300px;
  height: auto; /* Adjusted for content */
  display: flex;
  flex-direction: column;
  align-items: center;
}
.modal-header {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
}
.modal-content {
  text-align: center;
  width: 100%;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  padding-bottom: 5px;
}

.modal-close-button {
  position: absolute;
  top: 10px;
  right: 15px;
  background: none;
  border: none;
  font-size: 25px;
  cursor: pointer;
}
</style>
