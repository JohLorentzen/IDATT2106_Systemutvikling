import axiosAPI from '@/services/api'
import type { LoginResponse, User, UserInsight, UserStats } from './types'

/**
 * API call for registering a user in the database.
 * @param user The user to be registered.
 * @returns 200 if OK.
 *          400 on internal server error (general error).
 *          409 if username already exists.
 */
async function registerUser(user: User) {
  const userRegistrationJSON = {
    fullName: user.fullName,
    username: user.email,
    password: user.password
  }
  return await axiosAPI.post('/auth/register', userRegistrationJSON)
}

/**
 * API call for logging in a registered user.
 * @param user The user to log in.
 * @returns 200 if OK, along with TokenPair containing an access and a refresh token.
 *          400 on internal server error (general error).
 *          401 if the password is incorrect.
 *          404 if the username/email does not exist.
 */
async function loginUser(user: User) {
  const userLoginJSON = {
    username: user.email,
    password: user.password
  }
  return await axiosAPI.post<LoginResponse>('/auth/login', userLoginJSON)
}

/**
 * Fetch the logged in user's user insight.
 * @returns 200 if ok, along with the logged in user's user insight.
 */
async function getUserInsight() {
  return await axiosAPI.get<UserInsight>('/rest/user-insight')
}

async function getUserStats() {
  return await axiosAPI.get<UserStats>('/rest/user-stats')
}
/**
 * Post/save/update user insight for the logged in user.
 * @param userInsight The user insight to assign to the logged in user.
 * @returns 201 if ok.
 */
async function postUserInsight(userInsight: UserInsight) {
  return await axiosAPI.post('/rest/user-insight', userInsight)
}

export default {
  registerUser,
  loginUser,
  getUserStats,
  getUserInsight,
  postUserInsight
}
