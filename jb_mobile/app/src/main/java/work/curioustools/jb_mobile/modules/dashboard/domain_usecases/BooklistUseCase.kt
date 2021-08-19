package work.curioustools.jb_mobile.modules.dashboard.domain_usecases

import work.curioustools.jb_mobile.modules.dashboard.data_apis.BookItemDto
import work.curioustools.jb_mobile.modules.dashboard.data_repos.DashboardRepo
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseConcurrencyUseCase
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseResponse
import javax.inject.Inject

class BookListUseCase @Inject constructor(private val repo: DashboardRepo) :
    BaseConcurrencyUseCase<Unit, BaseResponse<List<BookItemDto>?>>() {

    override suspend fun getRepoCall(param: Unit) = repo.getBooksList()
}