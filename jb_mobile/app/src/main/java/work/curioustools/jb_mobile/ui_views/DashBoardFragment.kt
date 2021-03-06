package work.curioustools.jb_mobile.ui_views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import work.curioustools.curiousutils.core_droidjet.arch.BaseCommonFragmentVB
import work.curioustools.curiousutils.core_droidjet.extensions.showToastFromFragment

import work.curioustools.curiousutils.core_droidjet.arch.BaseListModel
import work.curioustools.curiousutils.core_droidjet.extensions.setVisible
import work.curioustools.jb_mobile.databinding.FragmentDashboardBinding
import work.curioustools.jb_mobile.data_apis.DashboardApi
import work.curioustools.jb_mobile.ui_adapters.AllBooksAdapter
import work.curioustools.jb_mobile.ui_models.BookModel
import work.curioustools.jb_mobile.ui_viewmodel.DashboardViewModel
import work.curioustools.third_party_network.arch_network.BaseResponse

@AndroidEntryPoint
class DashBoardFragment : BaseCommonFragmentVB<FragmentDashboardBinding>(){

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private val dashboardAdp = AllBooksAdapter(DashboardApi.BASE_URL, ::onBookClick)

    override fun getBindingForThisComponent(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)= FragmentDashboardBinding.inflate(inflater,container,false)

    override fun setup() {
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