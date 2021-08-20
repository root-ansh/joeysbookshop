package work.curioustools.jb_mobile.modules.dashboard.domain_usecases

import work.curioustools.jb_mobile.modules.dashboard.data_apis.BookItemDto
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DeleteBookRequest
import work.curioustools.jb_mobile.modules.dashboard.data_apis.SearchBooksRequest
import work.curioustools.jb_mobile.modules.dashboard.data_repos.DashboardRepo
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseConcurrencyUseCase
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseDto
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseResponse
import javax.inject.Inject

class BookListUseCase @Inject constructor(private val repo: DashboardRepo) :
    BaseConcurrencyUseCase<Unit, BaseResponse<List<BookItemDto>?>>() {

    override suspend fun getRepoCall(param: Unit) = repo.getBooksList()
}


class SearchBooksUseCase @Inject constructor(private val repo: DashboardRepo) :
    BaseConcurrencyUseCase<SearchBooksRequest, BaseResponse<List<BookItemDto>?>>() {

    override suspend fun getRepoCall(param: SearchBooksRequest) = repo.searchBooks(param)
}

class UpdateBookUseCase @Inject constructor(private val repo: DashboardRepo) :
    BaseConcurrencyUseCase<BookItemDto, BaseResponse<BaseDto?>>() {

    override suspend fun getRepoCall(param: BookItemDto) = repo.updateBook(param)
}


class DeleteBookUseCase @Inject constructor(private val repo: DashboardRepo) :
    BaseConcurrencyUseCase<DeleteBookRequest, BaseResponse<BaseDto?>>() {

    override suspend fun getRepoCall(param: DeleteBookRequest) = repo.deleteBook(param)
}