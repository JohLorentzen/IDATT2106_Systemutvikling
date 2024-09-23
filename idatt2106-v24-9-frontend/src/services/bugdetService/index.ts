import axiosAPI from '@/services/api'
import type { BudgetPostDTO } from '@/services/bugdetService/types'

async function getBudgetByMonthAndYear(month: number, year: number) {
  return await axiosAPI.get('/rest/budget-by-month-and-year?month=' + month + '&year=' + year)
}

async function updateBudgetPost(budgetPostDTO: BudgetPostDTO, budgetId?: number) {
  return await axiosAPI.post('/rest/update-budget-post?budgetId=' + budgetId, budgetPostDTO)
}

async function addBudgetPost(budgetPostDTO: BudgetPostDTO, budgetId?: number) {
  return await axiosAPI.post('/rest/add-budget-post?budgetId=' + budgetId, budgetPostDTO)
}

async function deleteBudgetPost(budgetId?: number, category?: string) {
  return await axiosAPI.delete(
    '/rest/delete-budget-post?budgetId=' + budgetId + '&category=' + category
  )
}

export default {
  getBudgetByMonthAndYear,
  updateBudgetPost,
  addBudgetPost,
  deleteBudgetPost
}
