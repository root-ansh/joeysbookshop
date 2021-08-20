package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

import android.util.Log
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

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
        logCurrentCall(" execute and unify called")
        val response: Response<T?> = execute()
        response.logRetrofitResponse()
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
        BaseResponse.Failure(null, AppResponseStatus.UNRECOGNISED.code,t)
    }

}


fun <DTO, RESP> BaseResponse<DTO>.convert(convertor: (DTO?) -> RESP?): BaseResponse<RESP?> {
    return when (this) {
        is BaseResponse.Failure -> BaseResponse.Failure(
            convertor.invoke(this.body),
            this.statusCode
        )
        is BaseResponse.Success -> BaseResponse.Success(convertor.invoke(this.body))
    }

}


inline fun <T> BaseResponse<T>.printResponse(onSuccess: (T) -> Unit = {}) {
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
    val TAG = "RETROFIT>>"
    this?.let {
        Log.e(TAG, "body = ${it.body()})")
        Log.e(TAG, "it.code = ${it.code()} ")
        Log.e(TAG, "it.isSuccessful = ${it.isSuccessful} ")
        Log.e(TAG, "msg = ${it.message()}")
        Log.e(TAG, "headers:")
        it.headers().toMultimap().forEach { (key, value) -> Log.e(TAG, "\t $key : $value") }
        Log.e(TAG, "it.errorBody = ${it.errorBody()} ")
        Log.e(TAG, "it.raw request = ${it.raw().request} ")
        Log.e(TAG, "it.raw request body= ${it.raw().request.body} ")
        Log.e(TAG, "it.raw request headers= ${it.raw().request.headers} ")
        Log.e(TAG, "it.raw response= ${it.raw()} ")

        Log.e(TAG, "it.raw response body= ${it.raw().body} ")
        Log.e(TAG, "it.raw response body msg= ${it.raw().message} ")

        Log.e(TAG, "===========================================")
    }
}

fun <T> Call<T>.logCurrentCall(msg:String =""){
    val TAG = "RETROFIT>>"
    Log.e(TAG, "logCurrentCall() called with: msg = $msg")
    this.let {
        it.request()?.let {
            Log.e(TAG, "logCurrentCall: body :  ${it.body}")
            Log.e(TAG, "logCurrentCall: cacheControl ${it.cacheControl}")
            it.headers.toMultimap().forEach { (key, value) -> Log.e(TAG, "\t $key : $value") }
            Log.e(TAG, "logCurrentCall: ishttps ${it.isHttps}")
            Log.e(TAG, "logCurrentCall: method ${it.method}")
            Log.e(TAG, "logCurrentCall: url ${it.url}")



        }
    }
}


/*
misc
     inline fun <reified T> Retrofit.createApi(): T = this.create(T::class.java)
    fun Retrofit.updateBaseUrl(baseUrl: String): Retrofit = this.newBuilder().baseUrl(baseUrl).build()

        @Suppress("DeprecatedCallableAddReplaceWith")
        @Deprecated("instead use the createRetrofitInstance")
        fun getSimpleRetrofitInstance(baseUrl: String): Retrofit {
            //i use it in personal projects, via gson
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createHttpClient())
                .addConverterFactory(createGsonConvertor())
                .build()
        }

        @Suppress("DeprecatedCallableAddReplaceWith")
        @Deprecated("instead use the createRetrofitInstance")
        fun getStandardRetrofitInstance(baseUrl: String): Retrofit {
            //used in standard professional projects via moshi
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(createMoshiConvertor())
                .client(createHttpClient())
                .build()
        }


        @Suppress("DeprecatedCallableAddReplaceWith")
        @Deprecated("instead use the extension")
        fun <T> getApiObject(
            apiDao: Class<T>, baseUrl: String, usingMoshi: Boolean = true
        ): T {
            val retrofit = createRetrofitInstance(baseUrl,  usingMoshi,  true)
            return retrofit.create(apiDao)
        }
*/