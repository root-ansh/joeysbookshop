package work.curioustools.jb_mobile.modules.dashboard.data_apis

import androidx.annotation.Keep

@Keep
data class SearchBooksRequest(
    val lang: String? = null,
    val price: String? = null,
    val country: String? = null,
    val author: String? = null,
    val title: String? = null,
    val minPages: Int? = null
)