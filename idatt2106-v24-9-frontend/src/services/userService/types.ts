import type { LifeSituation, Interest, Category, SkillLevel } from '@/enums/enums'

/**
 * Represents a user.
 */
export type User = {
  email: string
  password: string
  fullName?: string | null
  phoneNumber?: string | null
  createdAt?: Date | null
  userInsight?: UserInsight | null
  userStat?: UserStats | null
}

/**
 * Represents data collected from the user insight survey.
 */
export type UserInsight = {
  id: number | null
  skillLevel: SkillLevel
  lifeSituation: LifeSituation
  interests: Interest[]
  categories: Category[]
}

/**
 * Represents data regarding the user stats.
 *
 */
export type UserStats = {
  id: number | null
  totalSaved: number
  completedChallenges: number
  completedGoals: number
}

/**
 * Represents the response from the login API call.
 */
export type LoginResponse = {
  token: string
  refreshToken: string
  fullName: string
}
