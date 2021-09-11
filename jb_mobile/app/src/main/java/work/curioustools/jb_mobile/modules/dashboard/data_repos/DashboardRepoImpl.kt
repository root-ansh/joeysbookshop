package work.curioustools.jb_mobile.modules.dashboard.data_repos

import work.curioustools.jb_mobile.modules.dashboard.data_apis.BookItemDto
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DashboardApi
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DeleteBookRequest
import work.curioustools.jb_mobile.modules.dashboard.data_apis.SearchBooksRequest
import work.curioustools.third_party_network.base_arch.BaseDto

import work.curioustools.third_party_network.base_arch.BaseResponse
import work.curioustools.third_party_network.base_arch.convertTo
import work.curioustools.third_party_network.extensions.executeAndUnify
import javax.inject.Inject


class DashboardRepoImpl @Inject constructor(private val dashboardApi: DashboardApi) :
    DashboardRepo {

    override suspend fun getBooksList(): BaseResponse<List<BookItemDto>?> {
        return dashboardApi.getBookList().executeAndUnify().convertTo  { it }
    }

    override suspend fun searchBooks(searchBooksRequest: SearchBooksRequest): BaseResponse<List<BookItemDto>?> {
        return dashboardApi.searchBooks(
            lang = searchBooksRequest.lang,
            price = searchBooksRequest.price,
            country = searchBooksRequest.country,
            author = searchBooksRequest.author,
            title = searchBooksRequest.title,
            minPages = searchBooksRequest.minPages
        ).executeAndUnify().convertTo { it }
    }

    override suspend fun updateBook(booksRequest: BookItemDto): BaseResponse<BaseDto?> {
        return dashboardApi.updateBook(booksRequest).executeAndUnify().convertTo { it }
    }

    override suspend fun deleteBook(booksRequest: DeleteBookRequest): BaseResponse<BaseDto?> {
        return dashboardApi.deleteBook(booksRequest).executeAndUnify().convertTo { it }
    }

}

interface DashboardRepo {
    suspend fun getBooksList(): BaseResponse<List<BookItemDto>?>
    suspend fun searchBooks(searchBooksRequest: SearchBooksRequest):BaseResponse<List<BookItemDto>?>
    suspend fun updateBook(booksRequest: BookItemDto):BaseResponse<BaseDto?>
    suspend fun deleteBook(booksRequest: DeleteBookRequest):BaseResponse<BaseDto?>

}