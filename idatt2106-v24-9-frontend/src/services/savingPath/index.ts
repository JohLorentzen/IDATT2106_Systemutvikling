import axiosAPI from '@/services/api'
import type {
  Challenge,
  SavingsGoal,
  UserChallenge,
  SavingsPath,
  CompletedUserChallenge
} from './types'

/**
 * Fetch the logged in user's user challenges.
 * @returns 200 if ok, along with array of user challenges.
 */
async function getUserChallenges() {
  return await axiosAPI.get<UserChallenge[]>('/rest/user-challenges')
}

/**
 * Post/save a user challenge.
 * @param userChallenge The user challenge to post/save.
 * @returns 200 if ok.
 */
async function postUserChallenge(userChallenge: UserChallenge) {
  return await axiosAPI.post<UserChallenge>('/rest/user-challenge', userChallenge)
}

/**
 * Fetch a (new) challenge, based on data stored in user insights.
 * @returns 200 if ok, along with a challenge.
 *          404 if no challenges could be found.
 */
async function getChallenge() {
  return await axiosAPI.get<Challenge>('/rest/challenge')
}

/**
 * Post/save a challenge.
 * @param challenge The challenge to post/save.
 * @returns 200 if the challenge was saved.
 */
async function postChallenge(challenge: Challenge) {
  return await axiosAPI.post<Challenge>('/rest/challenge', challenge)
}

/**
 * Fetch the logged in user's active savings goal.
 * @returns 200 if ok, along with the active savings goal.
 */
async function getActiveSavingsGoal() {
  return await axiosAPI.get<SavingsGoal>('/rest/savings-goal')
}

async function getIncompleteSavingsGoals() {
  return await axiosAPI.get<SavingsGoal[]>('/rest/current-saving-goals')
}

async function getArchivedSavingsGoals() {
  return await axiosAPI.get<SavingsGoal[]>('/rest/archived-saving-goals')
}

/**
 * Post/save/update a savings goal. (Links to the logged in user?)
 * @param savingsGoal The savings goal to post/save.
 * @returns 201 if the savings goal was saved.
 */
async function postActiveSavingsGoal(savingsGoal: SavingsGoal) {
  return await axiosAPI.post<SavingsGoal>('/rest/savings-goal', savingsGoal)
}

async function deleteSavingsGoal(id: number) {
  return await axiosAPI.delete(`/rest/savings-goal/${id}`)
}

async function getSavingsPath() {
  return await axiosAPI.get<SavingsPath>('/rest/savings-path')
}

async function postCompletedUserChallenge(userChallenge: UserChallenge) {
  return await axiosAPI.post<CompletedUserChallenge>(
    '/rest/savings-path/completed-challenge',
    userChallenge
  )
}

async function getCompletedUserChallenges() {
  return await axiosAPI.get<CompletedUserChallenge[]>('/rest/savings-path/completed-challenges')
}
async function postRejectedChallenge(challenge: Challenge) {
  return await axiosAPI.post<Challenge>('/rest/challenge/reject', challenge)
}
async function acceptChallenge(userChallenge: UserChallenge) {
  return await axiosAPI.post<UserChallenge>('/rest/challenge/accept', userChallenge)
}

async function calculateAmountSaved() {
  return await axiosAPI.get('/rest/user-challenges/calculate')
}
export default {
  acceptChallenge,
  postRejectedChallenge,
  getUserChallenges,
  postUserChallenge,
  getChallenge,
  postChallenge,
  getActiveSavingsGoal,
  getIncompleteSavingsGoals,
  getArchivedSavingsGoals,
  postActiveSavingsGoal,
  deleteSavingsGoal,
  getSavingsPath,
  postCompletedUserChallenge,
  getCompletedUserChallenges,
  calculateAmountSaved
}
