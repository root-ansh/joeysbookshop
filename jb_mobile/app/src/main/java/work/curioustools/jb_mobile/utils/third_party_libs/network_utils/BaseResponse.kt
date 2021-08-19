package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

//<HELPER_CLASSES> =================================================================================
sealed class BaseResponse<T>(
    open val statusCode: Int,
    open var statusMsg: String
) {
    // base class which helps in unification of various network responses

    data class Success<T>(val body: T) : BaseResponse<T>(
        AppResponseStatus.SUCCESS.code,
        AppResponseStatus.SUCCESS.msg
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