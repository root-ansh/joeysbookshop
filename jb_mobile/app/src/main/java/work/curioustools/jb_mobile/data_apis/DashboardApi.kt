package work.curioustools.jb_mobile.data_apis

import retrofit2.Call
import retrofit2.http.*
import work.curioustools.third_party_network.arch_network.BaseDto

interface DashboardApi {

    @GET(BOOKLIST)
    fun getBookList(): Call<List<BookItemDto>>

    @GET(BOOKLIST_LANG)
    fun filterBookListByLanguage(@Path(LANGUAGE) id: String): Call<List<BookItemDto>>

    @GET(BOOKLIST)
    fun searchBooks(
        @Query("language") lang: String? = null,
        @Query("price") price: String? = null,
        @Query("country") country: String? = null,
        @Query("author") author: String? = null,
        @Query("title") title: String? = null,
        @Query("minPages") minPages: Int? = null,
    ): Call<List<BookItemDto>>

    @POST(UPDATE_BOOK)
    fun updateBook(@Body bookItemDto: BookItemDto): Call<BaseDto>

    @POST(DELETE_BOOK)
    fun deleteBook(@Body id: DeleteBookRequest): Call<BaseDto>

    companion object {
        const val BASE_URL = "https://joeysbookshop.herokuapp.com/"

        private const val API = "api"
        private const val BOOKLIST = "$API/booklist"
        private const val LANGUAGE = "id"
        private const val BOOKLIST_LANG = "$BOOKLIST/{$LANGUAGE}"
        private const val UPDATE_BOOK = "$API/update_book"
        private const val DELETE_BOOK = "$API/delete_book"
    }
}



