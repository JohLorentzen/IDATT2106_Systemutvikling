import axios, { AxiosError } from 'axios'
import router from '@/router/index.js'

class AuthInterceptor {
  refreshToken: string | null
  accessToken: string | null

  constructor() {
    this.accessToken = sessionStorage.getItem('accessToken')
    this.refreshToken = sessionStorage.getItem('refreshToken')
  }

  /**
   * Refreshes the access token by sending a request to the server.
   * If the request is successful, the new token is saved in the session storage.
   * If the request fails, the user is redirected to the login page.
   */
  async refreshAccessToken() {
    try {
      const response = await axios.post('http://129.241.98.8:8080/auth/refresh', {
        refreshToken: sessionStorage.getItem('refreshToken')
      })

      if (response.status === 200) {
        const token = response.data.token

        sessionStorage.setItem('accessToken', token)
        this.accessToken = token
      }
    } catch (error: unknown) {
      if (error instanceof AxiosError && error.response) {
        if (error.response.status === 403) {
          console.error('Token refresh failed - Forbidden, redirecting to login page')
          sessionStorage.clear()
          await router.push('/login')
        }
      }
    }
  }

  /**
   * Checks if the access token is expired.
   * @param token The token to check.
   */
  isTokenExpired(token: string) {
    if (!token) {
      return true
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]))

      const expirationTime = payload.exp * 1000

      const currentTime = Date.now()

      return currentTime >= expirationTime
    } catch (error) {
      console.error('Error decoding token:', error)
      return true
    }
  }
}
export default AuthInterceptor
