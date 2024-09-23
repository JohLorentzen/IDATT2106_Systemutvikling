/**
 * Represents a challenge, that is to be displayed and offered to the user.
 */
export type Challenge = {
  id?: number | null
  name: string
  description: string
  category: string
  duration: number
  sum: number
  averageAmount: number
  lifeSituation: string
  tip: string
}

/**
 * Represents a challenge that has been accepted by the user.
 */
export type UserChallenge = {
  id?: number | null
  goalAmount: number
  savedAmount: number
  fromDate: Date
  toDate: Date
  challenge: Challenge
}

/**
 * Represents a completed user challenge.
 */
export type CompletedUserChallenge = {
  id: number | null
  userChallenge: UserChallenge
  pointOfCompletion: number
  cx?: number
  cy?: number
}

/**
 * Represents a savings goal.
 */
export type SavingsGoal = {
  id?: number | null
  title: string
  goalAmount: number
  savedAmount: number
  endDate: Date
  daysLeft: number
  active: boolean
  isTargetReached: boolean
  dateAchieved?: Date
}

export class SavingsGoalImpl implements SavingsGoal {
  id?: number
  title: string
  goalAmount: number
  savedAmount: number
  endDate: Date
  daysLeft: number
  active: boolean
  isTargetReached: boolean
  dateAchieved?: Date

  constructor() {
    this.title = ''
    this.goalAmount = 0
    this.savedAmount = 0
    this.endDate = new Date()
    this.daysLeft = 0
    this.active = true
    this.isTargetReached = false
    this.dateAchieved = undefined
  }
}

/**
 * Represents a savings path.
 */
export type SavingsPath = {
  id?: number | null
  savingsGoal: SavingsGoal
  completedChallenges: CompletedUserChallenge[]
}
