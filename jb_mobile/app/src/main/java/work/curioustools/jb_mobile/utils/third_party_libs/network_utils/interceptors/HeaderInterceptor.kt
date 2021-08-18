package work.curioustools.jb_mobile.utils.third_party_libs.network_utils.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val appHeaders: HashMap<String, String>
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        return synchronized(this) {
            val originalRequest = chain.request()
            val requestBuilder =
                if (originalRequest.header(HEADER_SKIP_ALL).equals(HEADER_SKIP_ALL)) {
                    originalRequest.newBuilder().removeHeader(HEADER_SKIP_ALL)
                } else {
                    originalRequest.newBuilder().apply {
                        appHeaders.forEach { (key, value) ->
                            header(key, value)
                        }
                    }
                }

            chain.proceed(requestBuilder.build())
        }
    }

    companion object {
        val HEADER_OS_PAIR = Pair("x-os", "android")
        val HEADER_CONTENT_TYPE_PAIR = Pair( "Content-Type","application/json")
        val HEADER_DUMMY_API_AUTH_PAIR = Pair("app-id", "60ce529e134125fd80fd95b1")//todo use gradle properties/gradle keystore/gradle keychain for this

        const val HEADER_SKIP_ALL = "x-skip-all"


        const val HEADER_AUTH = "Authorization"
        const val HEADER_APP_VERSION = "x-app-version"
        const val ACCESS_COOKIE = "x-access-cookie"
        const val CACHE_CONTROL = "cache-control"
        const val CACHE_CONTROL_NO_CACHE = "no-cache"
        const val HEADER_TIME_ZONE = "x-timezone-offset"
        const val HEADER_USER_KIND = "x-user-kind"
        const val HEADER_APPLICATION_NAME = "x-application-name"
        const val APPLICATION_NAME = "APP_NAME"
    }

    private fun getToken(): String {
        return "app is sending this string as a token yolo"
        //todo: either return a token from shared prefs or
        //  generate token via firebase apis and save it in shared pref ,
        //  if shared pref token is null/empty. for api call, fallback to empty token in try catch
    }


}