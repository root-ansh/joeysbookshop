package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.interceptors.HeaderInterceptor
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.interceptors.InternetAvailabilityInterceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

//<DI> =============================================================================================
class NetworkDIManual {
    /*
     * CREATE == a function providing the dependency based on external checks  and other
     *           dependencies
     * GET ==  a function providing the dependency directly
     */

    fun createHttpClient(
        headers: HashMap<String, String>? = null,
        useUnSafeSocket: Boolean = false
    ): OkHttpClient {
        return OkHttpClient.Builder().run {
            connectTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
            retryOnConnectionFailure(true)
            addInterceptor(createLogInterceptor())
            if (headers != null) {
                addInterceptor(HeaderInterceptor(headers))
            }
            if (useUnSafeSocket) {
                try {
                    val (factory, trustManager) = getSocketFactoryAndUnsafeTrustManager()
                    sslSocketFactory(factory, trustManager)
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            build()
        }
    }

    /**
     * Requires implementation("com.squareup.okhttp3:okhttp:4.9.1")
     * RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
     * note: "does not work correctly", and makes the okhttp client untestable via direct jvm
     * */
    fun getInternetAvailabilityInterceptor(ctx: Context) = InternetAvailabilityInterceptor(ctx)


    fun createLogInterceptor(severity: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = severity
        return httpLoggingInterceptor
    }

    fun createGsonConvertor(gson: Gson = getGson()): GsonConverterFactory? {
        return GsonConverterFactory.create(gson)
    }
    fun createMoshiConvertor(mosh: Moshi = getMoshi()): MoshiConverterFactory? {
        return MoshiConverterFactory.create(mosh)
    }
    fun getScalerConvertor(): ScalarsConverterFactory? {
        return ScalarsConverterFactory.create()
    }

    private fun getGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    private fun getMoshi() = Moshi.Builder().build()

    private fun getSocketFactoryAndUnsafeTrustManager(): Pair<SSLSocketFactory, X509TrustManager> {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) = Unit

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) = Unit

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )
        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        val tm: X509TrustManager = trustAllCerts[0] as X509TrustManager

        return Pair(sslSocketFactory, tm)
    }

}