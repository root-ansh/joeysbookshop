package work.curioustools.jb_mobile.domain_usecases

import work.curioustools.jb_mobile.data_apis.BookItemDto
import work.curioustools.jb_mobile.data_apis.DeleteBookRequest
import work.curioustools.jb_mobile.data_apis.SearchBooksRequest
import work.curioustools.jb_mobile.data_repos.DashboardRepo
import work.curioustools.third_party_network.arch_network.BaseConcurrencyUseCase
import work.curioustools.third_party_network.arch_network.BaseDto
import work.curioustools.third_party_network.arch_network.BaseResponse
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