<template>
  <div
    @click="emitCardData"
    ref="cardElement"
    class="bg-white shadow-lg w-64 h-64 rounded-lg p-4 flex flex-col items-center justify-between m-2 cursor-pointer hover:bg-gray-100"
  >
    <div class="text-center my-2">
      <h2 class="text-lg font-bold truncate">{{ props.category.norwegianText }}</h2>
    </div>
    <div class="relative w-full h-full">
      <svg viewBox="0 0 36 36" class="absolute inset-0 w-full h-full">
        <circle
          cx="18"
          cy="18"
          r="15.91549430918954"
          fill="none"
          stroke="#eee"
          stroke-width="3"
        ></circle>
        <circle
          :id="`circle-${props.budgetPost.id}`"
          cx="18"
          cy="18"
          r="15.91549430918954"
          fill="none"
          :stroke="strokeColor"
          stroke-width="3"
          stroke-dasharray="100, 100"
          stroke-dashoffset="100"
          stroke-linecap="round"
          transform="rotate(-90 18 18)"
        ></circle>
      </svg>

      <div class="absolute inset-0 flex flex-col items-center justify-center">
        <h4 class="text-xs text-primary-text">Gjenstående</h4>
        <h2 class="text-xl font-bold -mt-1">
          {{ formatNumber(props.budgetPost.budgetedSum - props.budgetPost.actualSum) }} kr
        </h2>
      </div>
    </div>
    <h2 class="text-s text-primary-text">
      Budsjettert {{ formatNumber(props.budgetPost.budgetedSum) }} kr
    </h2>
  </div>
</template>

<script setup lang="ts">
import type { Category } from '@/enums/enums'
import { computed, nextTick, onMounted, ref, watch, watchEffect } from 'vue'
import type { BudgetPostDTO } from '@/services/bugdetService/types'
import { formatNumber } from '@/util/NumberFormatter'

const props = defineProps<{
  category: Category
  budgetPost: BudgetPostDTO
}>()

const emit = defineEmits(['selectCard'])

const emitCardData = () => {
  emit('selectCard', props.budgetPost)
}

/**
 * Interpolates between two colors based on a ratio.
 * @param color1 The first color in hex format.
 * @param color2 The second color in hex format.
 * @param ratio The ratio between the two colors.
 * @returns The interpolated color in rgb format.
 */
function interpolateColor(color1: string, color2: string, ratio: number) {
  const hexToRgb = (hex: string) => {
    const result = hex
      ?.replace(
        /^#?([a-f\d])([a-f\d])([a-f\d])$/i,
        (m: string, r: number, g: number, b: number) => '#' + r + r + g + g + b + b
      )
      ?.substring(1)
      ?.match(/.{2}/g)
      ?.map((x) => parseInt(x, 16))

    return result || [0, 0, 0]
  }

  const [r1, g1, b1] = hexToRgb(color1)
  const [r2, g2, b2] = hexToRgb(color2)
  const r = Math.round(r1 + (r2 - r1) * ratio)
  const g = Math.round(g1 + (g2 - g1) * ratio)
  const b = Math.round(b1 + (b2 - b1) * ratio)
  return `rgb(${r}, ${g}, ${b})`
}

const strokeDashOffset = ref(calculateStrokeDashOffset())

/**
 * Calulates the stroke dash offset based on the actual and budgeted sum.
 */
function calculateStrokeDashOffset() {
  const ratio = props.budgetPost.actualSum / props.budgetPost.budgetedSum
  return Math.max(0, 100 - ratio * 100)
}

/**
 * Animates the stroke dash offset of the circle.
 */
function animateStrokeDashOffset() {
  const element = document.getElementById(`circle-${props.budgetPost.id}`)
  if (element) {
    const currentOffset = parseFloat(element.getAttribute('stroke-dashoffset') || '100')
    const newOffset = calculateStrokeDashOffset()
    const duration = 1000 // duration of the animation in milliseconds

    const updateAnimation = (startTime: number) => (time: number) => {
      const timeElapsed = time - startTime
      const progress = Math.min(timeElapsed / duration, 1)
      const offset = currentOffset + (newOffset - currentOffset) * progress
      if (element) {
        element.setAttribute('stroke-dashoffset', offset.toString())
      }
      if (timeElapsed < duration) {
        requestAnimationFrame(updateAnimation(startTime))
      }
    }

    requestAnimationFrame(updateAnimation(performance.now()))
  }
}

watch(
  () => props.budgetPost,
  () => {
    nextTick(() => {
      animateStrokeDashOffset()
    })
  },
  { deep: true }
)

onMounted(() => {
  animateStrokeDashOffset()
})
/**
 * The stroke color for the circle.
 */
const strokeColor = computed(() => {
  const ratio = props.budgetPost.actualSum / props.budgetPost.budgetedSum
  if (ratio < 0.75) {
    return 'rgb(34, 197, 94)' // Green
  } else if (ratio < 0.9) {
    return interpolateColor('#22c55e', '#facc15', (ratio - 0.75) / 0.15) // from Green to Yellow
  } else if (ratio > 1) {
    return 'rgb(239, 68, 68)' // Red
  } else {
    return interpolateColor('#facc15', '#ef4444', (ratio - 0.9) / 0.1) // from Yellow to Red
  }
})

/**
 * The key for the animation.
 */
const animationKey = ref(0)

/**
 * Updates the stroke dash offset and the animation key.
 */
watchEffect(() => {
  document.documentElement.style.setProperty('--stroke-dashoffset-end', `${strokeDashOffset.value}`)
  animationKey.value++
})
</script>

<style scoped>
@keyframes draw-circle {
  from {
    stroke-dashoffset: 100;
  }
  to {
    stroke-dashoffset: var(--stroke-dashoffset-end, 100);
  }
}

.circle-animation {
  animation: draw-circle 1s ease-out forwards;
}
</style>
