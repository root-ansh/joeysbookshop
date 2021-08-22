package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseHiltFragment
import work.curioustools.jb_mobile.commons.BaseListModel
import work.curioustools.jb_mobile.databinding.FragmentSearchBinding
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DashboardApi
import work.curioustools.jb_mobile.modules.dashboard.data_apis.SearchBooksRequest
import work.curioustools.jb_mobile.modules.dashboard.ui_adapters.AllBooksAdapter
import work.curioustools.jb_mobile.modules.dashboard.ui_models.BookModel
import work.curioustools.jb_mobile.modules.dashboard.ui_viewmodel.DashboardViewModel
import work.curioustools.jb_mobile.utils.*
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseResponse
import work.curioustools.jb_mobile.utils.third_party_libs.toJson

@SuppressLint("SetTextI18n")
class SearchFragment : BaseHiltFragment(), VBHolder<FragmentSearchBinding> by VBHolderImpl() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val dashboardAdp = AllBooksAdapter(DashboardApi.BASE_URL, ::onBookClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSearchBinding.inflate(inflater, container, false).registeredRoot(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initUI

        withBinding {

            searchShimmer.apply {
                stopShimmer()
                setVisible(false)

            }
            rvSearchResults.apply {
                layoutManager = AllBooksAdapter.getLayoutManager(context,dashboardAdp)
                adapter = dashboardAdp
            }
            etSearchTitle.addTextChangedListener(StateUpdateWatcher(ivSearchTitle))
            etSearchCountry.addTextChangedListener(StateUpdateWatcher(ivSearchCountry))
            etSearchLang.addTextChangedListener(StateUpdateWatcher(ivSearchLang))
            etMinPages.addTextChangedListener(StateUpdateWatcher(ivSearchPages))
            etSearchAuthor.addTextChangedListener(StateUpdateWatcher(ivSearchAuthor))


            btSearch.setOnClickListener {
                val searchBooksRequest = SearchBooksRequest(
                    lang = etSearchLang.text?.toString(),
                    price = null,
                    country = etSearchCountry.text?.toString(),
                    author = etSearchAuthor.text?.toString(),
                    title = etSearchTitle.text?.toString(),
                    minPages = etMinPages.text?.toString()?.toIntSafe()
                )
                dashboardViewModel.searchForBook(searchBooksRequest)
                updateState(SearchResultsState.SEARCHING_FOR_DATA)
            }

            updateState(SearchResultsState.SCREEN_OPEN)
        }

        dashboardViewModel.searchBookResultsLiveData.observe(viewLifecycleOwner,::onBookListResponse)

    }

    private fun onBookListResponse(response: BaseResponse<List<BookModel>>?) {

        when(response){
            is BaseResponse.Success -> {
                if(response.body.isEmpty()) updateState(SearchResultsState.EMPTY_SEARCH_RESULTS)
                else updateState(SearchResultsState.DATA_AVAILABLE)
                dashboardAdp.updateAllEntries(response.body)
            }

            else ->{
                showToastFromFragment(response?.statusMsg?:"")
                updateState(SearchResultsState.SCREEN_OPEN)
            }
        }
    }


    private fun onBookClick(model: BaseListModel) {
        binding?.rvSearchResults?.showToastFromView(model.toJson())
    }


    private fun updateState(state:SearchResultsState){
        withBinding {
            when(state){
                SearchResultsState.SCREEN_OPEN -> {
                    noSearchesLayout.apply {
                        root.setVisible(true)
                        tvMessage.text = "Enter some filter to search"
                    }

                    searchShimmer.stopShimmer()
                    searchShimmer.setVisible(false)

                    rvSearchResults.setVisible(false)

                }

                SearchResultsState.SEARCHING_FOR_DATA -> {
                    searchShimmer.setVisible(true)
                    searchShimmer.startShimmer()

                    rvSearchResults.setVisible(false)
                    noSearchesLayout.root.setVisible(false)
                }
                SearchResultsState.DATA_AVAILABLE -> {
                    rvSearchResults.setVisible(true)

                    searchShimmer.stopShimmer()
                    searchShimmer.setVisible(false)

                    noSearchesLayout.root.setVisible(false)
                }
                SearchResultsState.EMPTY_SEARCH_RESULTS ->{
                    noSearchesLayout.apply {
                        root.setVisible(true)
                        tvMessage.text = "No entries available"
                    }

                    searchShimmer.stopShimmer()
                    searchShimmer.setVisible(false)

                    rvSearchResults.setVisible(false)

                }

            }
        }

    }

    enum class SearchResultsState{SEARCHING_FOR_DATA,DATA_AVAILABLE,EMPTY_SEARCH_RESULTS,SCREEN_OPEN}
    class StateUpdateWatcher(private val checkerView:ImageView):TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(text.isNullOrBlank()){
                checkerView.setImageResource(R.drawable.ic_check_circle_unfilled)
            }
            else{
                checkerView.setImageResource(R.drawable.ic_check_circle_filled)
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }
}

