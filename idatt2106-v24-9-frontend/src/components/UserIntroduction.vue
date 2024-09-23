<template>
  <div
    class="min-h-screen bg-backgroundImage bg-cover bg-no-repeat bg-center flex flex-col items-center justify-start px-6 text-primary-text"
  >
    <div class="w-full max-w-lg bg-white p-6 rounded-lg shadow-lg border border-gray-300 mt-20">
      <form @submit.prevent="handleNext" class="space-y-8">
        <div v-if="currentStep === 1">
          <span class="flex justify-center text-center text-xl mb-6 font-bold mt-10">{{
            textData.userIntroduction.savingSkillLabel
          }}</span>
          <span class="block text-center mb-4">
            {{ getSkillLabel(formData.skillLevel) }}
          </span>
          <input
            type="range"
            min="0"
            max="4"
            v-model="sliderIndex"
            @input="updateSkillLevel"
            class="w-full"
          />
        </div>

        <div v-if="currentStep === 2">
          <label
            for="lifeSituation"
            class="flex justify-center text-center text-xl mb-6 font-bold mt-10"
            >{{ textData.userIntroduction.lifeSituationLabel }}</label
          >
          <select
            id="lifeSituation"
            v-model="formData.lifeSituation"
            class="w-full rounded-lg border border-gray-300 p-3 bg-white focus:ring-2 focus:ring-indigo-500 mb-2"
          >
            <option disabled value="">{{ textData.userIntroduction.placeholder }}</option>
            <option v-for="option in lifeSituationOptions" :key="option.name" :value="option.name">
              {{ option.norwegianText }}
            </option>
          </select>
        </div>

        <button
          type="submit"
          :disabled="!isCurrentStepValid"
          :class="[
            'w-full',
            'rounded-lg',
            'py-3',
            'text-white',
            isCurrentStepValid ? 'bg-primaryBtn hover:bg-hooverBtn' : 'bg-gray-400'
          ]"
        >
          {{ textData.userIntroduction.submitButtonText }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import textData from '@/assets/text.no.json'
import api from '@/services/enumService/index'
import type { LifeSituation } from '@/enums/enums'

const emit = defineEmits(['update-insights', 'next'])

const lifeSituationOptions = ref([] as LifeSituation[])
const currentStep = ref(1)

const savingSkillOptions = [
  ['Nybegynner', 'BEGINNER'],
  ['Litt erfaren', 'INTERMEDIATE'],
  ['Gjennomsnittlig', 'AVERAGE'],
  ['Erfaren', 'ADVANCED'],
  ['Ekspert', 'EXPERT']
]

const sliderIndex = ref(0)

const formData = ref({
  skillLevel: savingSkillOptions[sliderIndex.value][1],
  lifeSituation: ''
})

const isCurrentStepValid = computed(() => {
  switch (currentStep.value) {
    case 1:
      return formData.value.skillLevel !== null
    case 2:
      return formData.value.lifeSituation !== ''
    default:
      return false
  }
})

/**
 * Returns the label for the selected skill level.
 * @param skillLevel The skill level to get the label for.
 * @returns The label for the skill level.
 */
const getSkillLabel = (skillLevel: string | null) => {
  const foundOption = savingSkillOptions.find((option) => option[1] === skillLevel)
  return foundOption ? foundOption[0] : ''
}

/**
 * Updates the skill level in the form data based on the slider index.
 */
const updateSkillLevel = () => {
  formData.value.skillLevel = savingSkillOptions[sliderIndex.value][1]
}

onMounted(() => {
  api
    .getLifeSituations()
    .then((response) => {
      lifeSituationOptions.value = response.data
    })
    .catch((error) => {
      console.error('Error fetching data:', error)
    })
})

/**
 * Handles the next button click on the survey.
 */
const handleNext = () => {
  if (currentStep.value < 2) {
    currentStep.value += 1
  } else {
    emit('update-insights', {
      skillLevel: formData.value.skillLevel,
      lifeSituation: formData.value.lifeSituation
    })
    emit('next')
  }
}

defineExpose({
  currentStep,
  formData,
  lifeSituationOptions,
  isCurrentStepValid,
  handleNext,
  getSkillLabel,
  updateSkillLevel,
  sliderIndex
})
</script>
