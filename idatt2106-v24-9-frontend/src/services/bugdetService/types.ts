export type BudgetDTO = {
  id?: number
  month: number
  year: number
  totalBudgetedSum: number
  totalSpentSum: number
  budgetPosts: BudgetPostDTO[]
}

export type BudgetPostDTO = {
  id?: number
  category: string
  budgetedSum: number
  actualSum: number
}
