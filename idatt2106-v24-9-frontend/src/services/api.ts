import axios from 'axios'
import router from '@/router/index'
import AuthInterceptor from '@/util/AuthInterceptor'
import { type AxiosResponse, AxiosError } from 'axios'

const authInterceptor = new AuthInterceptor()

const axiosAPI = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT
})

/**
 * Interceptor for outgoing HTTP-requests.
 * Attempts to add JWT to Authorization header, if present.
 */
axiosAPI.interceptors.request.use(
  async (config) => {
    const accessToken = sessionStorage.getItem('accessToken')

    // Add accessToken to Authorization header in the request (if a token is present in the session storage)
    if (accessToken != '' && accessToken != null) {
      config.headers.Authorization = `Bearer ${accessToken}`
      config.headers.Accept = 'application/json'
      config.headers['Content-Type'] = 'application/json'
    }

    return config
  },
  (error: unknown) => {
    Promise.reject(error)
  }
)

/**
 * Interceptor for incoming HTTP-responses.
 * Catches 403 Forbidden and attempts to refresh JWT before attempting the original request again.
 * Redirects to login page if 403 Forbidden is received from refreshToken-request.
 */
axiosAPI.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  async function (error: any) {
    console.log('Error: ' + error)
    const originalRequest = error.config

    if (error instanceof AxiosError && error.response) {
      // Attempt to refresh accessToken and then run original request
      if (error.response.status === 403 && !originalRequest._retry) {
        originalRequest._retry = true

        await authInterceptor.refreshAccessToken()

        const accessToken = sessionStorage.getItem('accessToken')

        if (accessToken != '' && accessToken != null) {
          axios.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken
        }

        // Retry the original request
        try {
          return await axiosAPI(originalRequest)
        } catch (retryError: any) {
          if (
            retryError instanceof AxiosError &&
            retryError.response &&
            retryError.response.status === 403
          ) {
            // If the retry also results in a 403 status, redirect to login page
            sessionStorage.clear()
            router.push('/login')
          }
          throw retryError
        }
      }
    }

    return Promise.reject(error)
  }
)
export default axiosAPI
