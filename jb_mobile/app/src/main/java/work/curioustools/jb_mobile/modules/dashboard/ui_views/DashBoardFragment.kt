package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import work.curioustools.jb_mobile.commons.BaseHiltFragment
import work.curioustools.jb_mobile.commons.BaseListModel
import work.curioustools.jb_mobile.databinding.FragmentDashboardBinding
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DashboardApi
import work.curioustools.jb_mobile.modules.dashboard.ui_adapters.AllBooksAdapter
import work.curioustools.jb_mobile.modules.dashboard.ui_models.BookModel
import work.curioustools.jb_mobile.modules.dashboard.ui_viewmodel.DashboardViewModel
import work.curioustools.jb_mobile.utils.*
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseResponse
import work.curioustools.jb_mobile.utils.third_party_libs.toJson

class DashBoardFragment : BaseHiltFragment(), VBHolder<FragmentDashboardBinding> by VBHolderImpl() {

    private val dashboardViewModel: DashboardViewModel by viewModels()// viewModels()
    private val dashboardAdp = AllBooksAdapter(DashboardApi.BASE_URL, ::onBookClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDashboardBinding.inflate(inflater, container, false).registeredRoot(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init ui
        binding?.rvDashBoard?.apply {
            val ctx = this.context
            layoutManager = LinearLayoutManager(ctx)
            adapter = dashboardAdp
        }
        //set listeners
        dashboardViewModel.bookListLiveData.observe(viewLifecycleOwner, ::onDataReceived)
    }

    private fun onDataReceived(response: BaseResponse<List<BookModel>>?) {
        when (response) {
            is BaseResponse.Failure -> showToastFromFragment(response.toString())
            is BaseResponse.Success -> {
                dashboardAdp.updateAllEntries(response.body)
            }
            null -> {
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dashboardViewModel.getBooksList()
    }

    private fun onBookClick(model: BaseListModel) {
        binding?.rvDashBoard?.showToastFromView(model.toJson())
    }
}