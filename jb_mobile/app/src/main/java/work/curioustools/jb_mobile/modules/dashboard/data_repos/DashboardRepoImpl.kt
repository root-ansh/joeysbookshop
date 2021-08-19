package work.curioustools.jb_mobile.modules.dashboard.data_repos

import work.curioustools.jb_mobile.modules.dashboard.data_apis.BookItemDto
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DashboardApi
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseResponse
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.convert
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.executeAndUnify
import javax.inject.Inject


class DashboardRepoImpl @Inject constructor(private val dashboardApi: DashboardApi) :
    DashboardRepo {

    override suspend fun getBooksList(): BaseResponse<List<BookItemDto>?> {
        return dashboardApi.getBookList().executeAndUnify().convert {
            it
        }
    }

}

interface DashboardRepo {
    suspend fun getBooksList(): BaseResponse<List<BookItemDto>?>
}