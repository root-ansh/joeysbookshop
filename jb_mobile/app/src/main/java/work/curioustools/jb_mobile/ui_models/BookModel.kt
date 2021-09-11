package work.curioustools.jb_mobile.ui_models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import work.curioustools.curiousutils.core_droidjet.arch.BaseListModel
import work.curioustools.jb_mobile.data_apis.BookItemDto

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
    val language: String,
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

fun BookItemDto.toModel(): BookModel {
    val itemDto = this

    return BookModel(
        price = itemDto.price ?: "",
        author = itemDto.author ?: "",
        country = itemDto.country ?: "",
        imageQueryUrl = itemDto.imageLink ?: "",
        wiki = itemDto.link ?: "",
        pages = itemDto.pages ?: 0,
        title = itemDto.title ?: "",
        summary = itemDto.summary ?: "",
        year = itemDto.year ?: 1900,
        id = itemDto.id ?: "",
        language = itemDto.language ?: "",
        tags = (itemDto.tags ?: listOf()).map { BookModel.BooksTagModel(it) },
        uiType = BookModel.BookUiType.default()
    )


}

fun BookModel.toDto(): BookItemDto {
    val bookModel = this
    return BookItemDto(
        price = bookModel.price,
        author = bookModel.author,
        country = bookModel.country,
        imageLink = bookModel.imageQueryUrl,
        language = bookModel.language,
        link = bookModel.wiki,
        pages = bookModel.pages,
        title = bookModel.title,
        summary = bookModel.summary,
        year = bookModel.year,
        id = bookModel.id,
        tags = bookModel.tags.map { it.tag }
    )

}
