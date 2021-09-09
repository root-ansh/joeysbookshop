package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

import retrofit2.Call
import retrofit2.Response
import work.curioustools.third_party_network.extensions.logCurrentCall
import work.curioustools.third_party_network.extensions.logRetrofitResponse



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