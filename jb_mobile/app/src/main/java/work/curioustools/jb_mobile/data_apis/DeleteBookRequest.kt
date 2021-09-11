package work.curioustools.jb_mobile.data_apis

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class DeleteBookRequest(@field:Json(name = "id")val id:String)