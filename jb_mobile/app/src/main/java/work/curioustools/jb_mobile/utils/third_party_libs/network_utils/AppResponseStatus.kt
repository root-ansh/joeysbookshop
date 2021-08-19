package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

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