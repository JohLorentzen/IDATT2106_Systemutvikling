<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center">
    <ToastComponent :position="toastPosition" />
    <div class="bg-white p-8 rounded-lg shadow-lg max-w-sm w-full mx-4">
      <FontAwesomeIcon
        icon="xmark"
        class="fa-xl float-right top-4 right-4 text-gray-500 hover:text-gray-800 cursor-pointer"
        @click="emits('close')"
      />
      <h2 class="text-2xl font-bold text-center mb-4 mt-6">
        {{ props.isCreateNew ? 'Opprett ny budsjettpost' : 'Oppdater budsjettpost' }}
      </h2>
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">
          Kategori
          <select
            v-model="budgetPost.category"
            required
            :disabled="!props.isCreateNew"
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          >
            <option v-for="category in categories" :key="category.name" :value="category.name">
              {{ category.norwegianText }}
            </option>
          </select>
        </label>
      </div>
      <div class="mb-6">
        <label class="block text-gray-700 text-sm font-bold mb-2">
          Beløp:
          <input
            type="number"
            v-model.number="budgetPost.budgetedSum"
            required
            :class="{'border-red-600': hasInvalidInput}"
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          />
        </label>
        <p v-if="hasInvalidInput" class="text-red-600">{{ text.invalidInput }}</p>
      </div>
      <button
        v-if="props.isCreateNew"
        @click="createBudgetPost"
        class="bg-primaryBtn hover:bg-primary-text text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
      >
        Opprett
      </button>
      <div v-else class="flex gap-2 justify-center">
        <button
          @click="updateBudgetPost"
          class="bg-primaryBtn hover:bg-primary-text text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          Oppdater
        </button>
        <button
          @click="deleteBudgetPost"
          class="sm:w-auto bg-white text-red-600 hover:ring-2 hover:ring-red-600 hover:shadow-lg rounded-md px-4 py-2 shadow-md border border-red-600 transition-all duration-200"
        >
          Slett
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import type { Category } from '@/enums/enums'
import api from '@/services/enumService'
import budgetApi from '@/services/bugdetService'
import type { BudgetPostDTO } from '@/services/bugdetService/types'
import { useToast } from 'primevue/usetoast'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { AxiosError } from 'axios'
import text from '@/assets/text.no.json'

const hasInvalidInput = ref<boolean>(false)
const categories = ref([] as Category[])
const emits = defineEmits(['close'])
const toast = useToast()
const screenWidth = ref(window.innerWidth)
const toastPosition = computed(() => {
  return screenWidth.value < 768 ? 'top-center' : 'bottom-right'
})

const props = defineProps<{
  isCreateNew: boolean
  budgetId: number | undefined
  budgetPost: BudgetPostDTO
}>()

const budgetPost = ref(JSON.parse(JSON.stringify(props.budgetPost)))

watch(
  [() => props.budgetPost],
  () => {
    budgetPost.value = JSON.parse(JSON.stringify(props.budgetPost))
  },
  {
    immediate: true
  }
)

/**
 * Fetches all categories from the API.
 */
const fetchCategories = async () => {
  const response = await api.getCategories()
  categories.value = response.data
}

/**
 * Updates the budget post.
 */
async function updateBudgetPost() {
  if (invalidInput()) {
    hasInvalidInput.value = true;
    return
  }

  try {
    await budgetApi.updateBudgetPost(budgetPost.value, props.budgetId)
    hasInvalidInput.value = false;
    emits('close')
  } catch (error) {
    console.error(error)
  }
}

/**
 * Creates a new budget post.
 */
async function createBudgetPost() {
  if (invalidInput()) {
    hasInvalidInput.value = true
    return
  }

  try {
    await budgetApi.addBudgetPost(budgetPost.value, props.budgetId)
    emits('close')
  } catch (error: unknown) {
    console.error(error)
    if (error instanceof AxiosError && error.response?.status === 409) {
      toast.add({
        severity: 'error',
        summary: 'Opprettelse feilet',
        detail: 'Det finnes allerede en budsjettpost for denne kategorien',
        life: 5000
      })
    }
  }
}

/**
 * Deletes the budget post.
 */
async function deleteBudgetPost() {
  try {
    await budgetApi.deleteBudgetPost(props.budgetId, budgetPost.value.category)
    emits('close')
  } catch (error) {
    console.error(error)
  }
}

/**
 * Checks if input is valid.
 */
function invalidInput() {
  return budgetPost.value.budgetedSum < 0 || budgetPost.value.budgetedSum == null
}

onMounted(fetchCategories)
</script>
