package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import work.curioustools.core_android.setVisible
import work.curioustools.core_android.showToastFromFragment

import work.curioustools.jb_mobile.commons.BaseHiltFragment
import work.curioustools.jb_mobile.commons.BaseListModel
import work.curioustools.jb_mobile.databinding.FragmentDashboardBinding
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DashboardApi
import work.curioustools.jb_mobile.modules.dashboard.ui_adapters.AllBooksAdapter
import work.curioustools.jb_mobile.modules.dashboard.ui_models.BookModel
import work.curioustools.jb_mobile.modules.dashboard.ui_viewmodel.DashboardViewModel
import work.curioustools.jetpack_lifecycles.VBHolder
import work.curioustools.jetpack_lifecycles.VBHolderImpl
import work.curioustools.third_party_network.base_arch.BaseResponse

class DashBoardFragment : BaseHiltFragment(), VBHolder<FragmentDashboardBinding> by VBHolderImpl() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
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
            layoutManager = AllBooksAdapter.getLayoutManager(ctx,dashboardAdp)
            adapter = dashboardAdp
        }
        //set listeners
        dashboardViewModel.bookListLiveData.observe(viewLifecycleOwner, ::onDataReceived)

        initCall()
    }

    fun initCall() {
        dashboardAdp.removeAllEntries()
        dashboardViewModel.getBooksList()
        getBindingOrError().shimmerDashboard.apply {
            startShimmer()
            setVisible(true)
        }
    }

    private fun onDataReceived(response: BaseResponse<List<BookModel>>?) {
        getBindingOrError().shimmerDashboard.apply {
            stopShimmer()
            setVisible(false)
        }
        when (response) {
            is BaseResponse.Success -> {
                dashboardAdp.updateAllEntries(response.body)
            }
            else -> {
                showToastFromFragment(response.toString())
            }
        }
    }

    private fun onBookClick(model: BaseListModel) {
        if (model is BookModel)
            DetailsActivity.startActivityForResult(requireActivity() as AppCompatActivity,model)
    }
}