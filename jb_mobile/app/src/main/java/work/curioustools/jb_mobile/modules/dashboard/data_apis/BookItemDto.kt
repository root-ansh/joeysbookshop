package work.curioustools.jb_mobile.modules.dashboard.data_apis

import androidx.annotation.Keep
import com.squareup.moshi.Json
import work.curioustools.third_party_network.arch_network.BaseDto

@Keep
data class BookItemDto(
    @field:Json(name = "price") val price: String? = null,
    @field:Json(name = "author") val author: String? = null,
    @field:Json(name = "country") val country: String? = null,
    @field:Json(name = "imageLink") val imageLink: String? = null,
    @field:Json(name = "language") val language: String? = null,
    @field:Json(name = "link") val link: String? = null,
    @field:Json(name = "pages") val pages: Int? = null,
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "summary") val summary: String? = null,
    @field:Json(name = "year") val year: Int? = null,
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "tags") val tags: List<String>? = null,
) : BaseDto()