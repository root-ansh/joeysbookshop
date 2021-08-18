@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package work.curioustools.jb_mobile.utils.third_party_libs.network_utils


import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.interceptors.HeaderInterceptor
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.interceptors.InternetAvailabilityInterceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.HashMap

//==================================================================================================
/*
REQUIREMENTS:
- "com.squareup.retrofit2:retrofit:2.9.0")
- "com.squareup.retrofit2:converter-gson:2.9.0")
- "com.squareup.retrofit2:converter-moshi:2.9.0")
- "com.squareup.retrofit2:converter-scalars:2.9.0")
- "com.squareup.okhttp3:okhttp:4.9.1")
- "com.squareup.okhttp3:logging-interceptor:4.9.1")
- "org.jsoup:jsoup:1.13.1")
*/
//<EXTENSIONS> =====================================================================================
fun <T> Class<T>.getApi(baseUrl: String, headers: HashMap<String, String>? = null): T {
    val retrofit = NetworkUtils.createRetrofitInstance(
        baseUrl = baseUrl,
        useMoshiConvertor = true,
        useScalerConverotr = true,
        headers = headers

    )
    return retrofit.create(this)
}
fun <T> Call<T>.executeAndUnify(): BaseResponse<T> {
    /**
     * Retrofit provides a response of format Response(isSuccessful:True/False, body:T/null,...)
     * it treats all failures as null . this Response object on its own is enough to know about the
     * json response, but for convenience we can use a unified sealed class for handling high level
     * distinctions,such as success, failure, token expire failure etc.
     * */
    return try {
        val response: Response<T?> = execute()
        when {
            response.isSuccessful -> {
                when (val body = response.body()) {
                    null -> BaseResponse.Failure(
                        body,
                        AppResponseStatus.APP_NULL_RESPONSE_BODY.code
                    )
                    else -> {
                        // there can be other scenarios also, like
                        //if(body is BaseDto && body.success == false){
                        //  BaseResponse.Failure(null,AppResponseStatus.UNRECOGNISED.code)
                        //}

                        BaseResponse.Success(body)
                    }
                }
            }
            else -> {
                val code = response.code()
                val msg =
                    AppResponseStatus.getStatusMsgOrDefault(code)
                // todo get server msg via error body and not from enum
                val body = response.body()
                if (body is BaseDto && body.error.isNullOrBlank()) {
                    // if body is of type BaseDto(which it should be), it will set
                    // error msg if not already set
                    body.error = msg
                }
                val finalResponse = BaseResponse.Failure(body, code)
                finalResponse.statusMsg = msg
                finalResponse.exception = Exception(msg)
                finalResponse
            }
        }
    } catch (t: Throwable) {
        BaseResponse.Failure(null, AppResponseStatus.UNRECOGNISED.code)
    }

}
inline fun <T> BaseResponse<T>.logResponse(onSuccess: (T) -> Unit = {}) {
    val time = Calendar.getInstance().time
    println("=====<test started at $time> ========")
    val resp = this
    println("response code =" + resp.statusCode)
    println("response msg= " + resp.statusMsg)

    when (resp) {
        is BaseResponse.Failure -> {
            println("response = ${resp.body}")
            println("exception = ${resp.exception}")
        }
        is BaseResponse.Success -> {
            (resp.body as? BaseDto)?.let {
                println("current response extends BaseDto.class")
                println("error:" + it.error)
                println("limit:" + it.limit)
                println("currentPage:" + it.currentPage)
                println("totalPages:" + it.totalPages)
                println("offset:" + it.offset)
                println("perPageEntries:" + it.perPageEntries)
                println("totalEntries:" + it.totalEntries)
            }
            onSuccess.invoke(resp.body)
        }
    }

    println("=====</end for test started at $time> ========")

}
fun <T> Response<T>?.logRetrofitResponse() {
    this?.let {
        println("body = ${it.body()})")
        println("it.code = ${it.code()} ")
        println("it.isSuccessful = ${it.isSuccessful} ")
        println("msg = ${it.message()}")
        println("headers:")
        it.headers().toMultimap().forEach { (key, value) -> println("\t $key : $value") }
        println("it.errorBody = ${it.errorBody()} ")
        println("it.raw request = ${it.raw().request} ")
        println("it.raw request body= ${it.raw().request.body} ")
        println("it.raw request headers= ${it.raw().request.headers} ")
        println("it.raw response= ${it.raw()} ")

        println("it.raw response body= ${it.raw().body} ")
        println("it.raw response body msg= ${it.raw().message} ")

        println("===========================================")
    }
}
//</EXTENSIONS> ====================================================================================

//<HELPER_CLASSES> =================================================================================
sealed class BaseResponse<T>(
    open val statusCode: Int,
    open var statusMsg: String
) {
    // base class which helps in unification of various network responses

    data class Success<T>(val body: T) : BaseResponse<T>(
        AppResponseStatus.SUCCESS.code,
        AppResponseStatus.getStatusMsgOrDefault(AppResponseStatus.SUCCESS.code)
    )

    data class Failure<T>(
        val body: T? = null,
        override val statusCode: Int,
    ) : BaseResponse<T>(statusCode, AppResponseStatus.getStatusMsgOrDefault(statusCode)) {
        var exception: Throwable = Exception(statusMsg)

        override fun toString(): String {
            return "Failure(body=$body, statusCode=$statusCode, statusMsg=${statusMsg}, exception=$exception)"
        }


    }
}
open class BaseDto(
    @field:Json(name = "error") open var error: String? = null,

    @field:Json(name = "page") open val currentPage: Int? = null, //currentPage
    @field:Json(name = "total_pages") open val totalPages: Int? = null,

    @field:Json(name = "per_page") open val perPageEntries: Int? = null,
    @field:Json(name = "total") open val totalEntries: Int? = null, //totalEntries
    @field:Json(name = "limit") val limit: Int? = null,
    @field:Json(name = "offset") val offset: Int? = null

) {
    /**
     * we can use this class as parent for list based dtos. i.e dtos which map the response of
     * api calls that returns a response like this:
    {
    "page":1,
    "total_pages":35,
    "total":3500,
    "per_page":100,
    "data":[ {object},{object},{object},...]
    }

    your dto will look very clean, like this:

    @Keep
    data class AllAuthUsersResponseDto(
    @field:Json(name = "data") val entries: List<UserDto>? = null
    ) : BaseDto(){

    @Keep
    data class UserDto(
    @field:Json(name = "id") val id: Int? = null,
    @field:Json(name = "email") val email: String? = null,
    ...
    )
    }
     *
     *
     *
     * */
}
enum class AppResponseStatus(val code: Int, val msg: String) {
    SUCCESS(200, "SUCCESS"),
    NO_INTERNET_CONNECTION(1001, "No Internet found"),
    USER_NOT_FOUND(400, "User Not Found"),
    APP_NULL_RESPONSE_BODY(888, "No Response found"),
    SERVER_FAILURE(500, "server failure"),
    SERVER_DOWN_502(502, "server down 502"),
    SERVER_DOWN_503(503, "server down 503"),
    SERVER_DOWN_504(504, "server down 504"),
    UNRECOGNISED(-1, "unrecognised error in networking");

    companion object {
        @Deprecated(
            message = "use nullable returns and provide server based error",
            replaceWith = ReplaceWith("getStatusMsgOrNull(code)"),
            level = DeprecationLevel.WARNING
        )
        fun getStatusMsgOrDefault(code: Int): String {
            return getStatusMsgOrNull(code) ?: UNRECOGNISED.msg
        }

        fun getStatusMsgOrNull(code: Int): String? {
            val enumVal = values().firstOrNull { it.code == code }
            return enumVal?.msg
        }
    }
}
//</HELPER_CLASSES> ================================================================================

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


    fun createLogInterceptor(severity: Level = Level.BODY): HttpLoggingInterceptor {
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
//</DI> ============================================================================================

//<UTILS> ==========================================================================================
class NetworkUtils {
    companion object {
        private val di = NetworkDIManual()

        fun createRetrofitInstance(
            baseUrl: String,
            useMoshiConvertor: Boolean = true,
            useScalerConverotr: Boolean = true,
            headers: HashMap<String, String>? = null
        ): Retrofit {

            val okHttpClient = di.createHttpClient(headers)
            val scalerConvertor = di.getScalerConvertor()
            val gsonConvertor = di.createGsonConvertor()
            val moshiConverter = di.createMoshiConvertor()
            return Retrofit.Builder().run {
                baseUrl(baseUrl)
                client(okHttpClient)
                if (useScalerConverotr && scalerConvertor != null) {
                    addConverterFactory(scalerConvertor)
                }
                val convertor = if (useMoshiConvertor) moshiConverter else gsonConvertor
                if (convertor != null) addConverterFactory(convertor)
                build()
            }

        }


        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        @SuppressWarnings("deprecation")
        fun isNetworkAvailable(ctx: Context): Boolean {
            val cm = ctx.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }

            } else {
                cm.activeNetworkInfo?.isConnectedOrConnecting == true
            }
        }


    }
}
//</UTILS> =========================================================================================

//<Example>=================================================================//
/*
    interface PokeApi{

        @GET(ENDPOINT_POKEMON_BY_ID)
        fun getPokemon(@Path(ID)id:Int): Call<String>

        @GET(POKEMON)
        fun getNextInfo(@Query(LIMIT) limit:Int): Call<String>


        companion object{
            //paths and query variables
            const val ID ="id"
            const val LIMIT = "limit"
            const val POKEMON = "pokemon"

            const val BASE = "https://pokeapi.co/api/v2/"


            const val ENDPOINT_POKEMON_BY_ID ="$POKEMON/{$ID}"

            fun main(){

                val api = PokeApi::class.java.getApi(PokeApi.BASE)
                val api2 = NetworkUtils.getApiObject(PokeApi::class.java,PokeApi.BASE)
                println(api.getNextInfo(1).execute().body())

            }
        }

    }
*/
//</Example>================================================================//


//misc
//     inline fun <reified T> Retrofit.createApi(): T = this.create(T::class.java)
//    fun Retrofit.updateBaseUrl(baseUrl: String): Retrofit = this.newBuilder().baseUrl(baseUrl).build()
//
//        @Suppress("DeprecatedCallableAddReplaceWith")
//        @Deprecated("instead use the createRetrofitInstance")
//        fun getSimpleRetrofitInstance(baseUrl: String): Retrofit {
//            //i use it in personal projects, via gson
//            return Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .client(createHttpClient())
//                .addConverterFactory(createGsonConvertor())
//                .build()
//        }
//
//        @Suppress("DeprecatedCallableAddReplaceWith")
//        @Deprecated("instead use the createRetrofitInstance")
//        fun getStandardRetrofitInstance(baseUrl: String): Retrofit {
//            //used in standard professional projects via moshi
//            return Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(createMoshiConvertor())
//                .client(createHttpClient())
//                .build()
//        }
//
//
//        @Suppress("DeprecatedCallableAddReplaceWith")
//        @Deprecated("instead use the extension")
//        fun <T> getApiObject(
//            apiDao: Class<T>, baseUrl: String, usingMoshi: Boolean = true
//        ): T {
//            val retrofit = createRetrofitInstance(baseUrl,  usingMoshi,  true)
//            return retrofit.create(apiDao)
//        }

//todo document:
// - why scaler convertors are used
// - why miscallanous things are used