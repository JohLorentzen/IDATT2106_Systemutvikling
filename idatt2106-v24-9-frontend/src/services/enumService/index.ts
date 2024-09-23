import type { LifeSituation, Category, Interest } from '@/enums/enums'
import axiosAPI from '@/services/api'

/**
 * Fetch all life situation enums currently stored in the database.
 * @returns 200 if ok, along with the enums.
 */
async function getLifeSituations() {
  return await axiosAPI.get<LifeSituation[]>('/enums/life-situations')
}

/**
 * Fetch all interest enums currently stored in the database.
 * @returns 200 if ok, along with the enums.
 */
async function getInterests() {
  return await axiosAPI.get<Interest[]>('/enums/interests')
}

/**
 * Fetch all category enums currently stored in the database.
 * @returns 200 if ok, along with the enums.
 */
async function getCategories() {
  return await axiosAPI.get<Category[]>('/enums/categories')
}
export default {
  getLifeSituations,
  getInterests,
  getCategories
}
