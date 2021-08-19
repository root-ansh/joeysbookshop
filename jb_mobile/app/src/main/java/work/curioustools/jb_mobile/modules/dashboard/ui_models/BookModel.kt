package work.curioustools.jb_mobile.modules.dashboard.ui_models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import work.curioustools.jb_mobile.commons.BaseListModel

@Keep
@Parcelize
data class BookModel(
    val price: String,
    val author: String,
    val country: String,
    val imageQueryUrl: String,
    val wiki: String,
    val pages: Int,
    val title: String,
    val summary: String,
    val year: Int,
    val id: String,
    val tags: List<BooksTagModel>,
    var uiType: BookUiType = BookUiType.default()
) : BaseListModel, Parcelable {

    @Keep
    @Parcelize
    data class BooksTagModel(val tag: String) : BaseListModel, Parcelable

    @Keep
    enum class BookUiType(val id: Int) {
        GRID(11),
        HORIZONTAL(12),
        HIGHLIGHT(13);

        companion object {
            fun default() = HORIZONTAL

            fun getTypeByID(id: Int): BookUiType {
                val type = values().firstOrNull { it.id == id }
                return type ?: default()
            }

        }
    }
}

