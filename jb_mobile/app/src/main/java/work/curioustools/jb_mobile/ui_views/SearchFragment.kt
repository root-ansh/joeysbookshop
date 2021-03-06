package work.curioustools.jb_mobile.ui_views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import work.curioustools.curiousutils.core_droidjet.arch.BaseCommonFragmentVB
import work.curioustools.curiousutils.core_droidjet.extensions.showToastFromFragment
import work.curioustools.jb_mobile.R
import work.curioustools.curiousutils.core_droidjet.arch.BaseListModel
import work.curioustools.curiousutils.core_droidjet.extensions.setVisible
import work.curioustools.jb_mobile.databinding.FragmentSearchBinding
import work.curioustools.jb_mobile.data_apis.DashboardApi
import work.curioustools.jb_mobile.data_apis.SearchBooksRequest
import work.curioustools.jb_mobile.ui_adapters.AllBooksAdapter
import work.curioustools.jb_mobile.ui_models.BookModel
import work.curioustools.jb_mobile.ui_viewmodel.DashboardViewModel
import work.curioustools.third_party_network.arch_network.BaseResponse

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class SearchFragment : BaseCommonFragmentVB<FragmentSearchBinding>(){

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val dashboardAdp = AllBooksAdapter(DashboardApi.BASE_URL, ::onBookClick)
    private var searchBooksRequest: SearchBooksRequest? = null

    override fun getBindingForThisComponent(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)= FragmentSearchBinding.inflate(inflater,container,false)

    override fun setup() {

        //initUI

        withBinding {

            searchShimmer.apply {
                stopShimmer()
                setVisible(false)

            }
            rvSearchResults.apply {
                layoutManager = AllBooksAdapter.getLayoutManager(context, dashboardAdp)
                adapter = dashboardAdp
            }
            etSearchTitle.addTextChangedListener(StateUpdateWatcher(ivSearchTitle))
            etSearchCountry.addTextChangedListener(StateUpdateWatcher(ivSearchCountry))
            etSearchLang.addTextChangedListener(StateUpdateWatcher(ivSearchLang))
            etMinPages.addTextChangedListener(StateUpdateWatcher(ivSearchPages))
            etSearchAuthor.addTextChangedListener(StateUpdateWatcher(ivSearchAuthor))


            btSearch.setOnClickListener {
                searchBooksRequest = SearchBooksRequest(
                    lang = etSearchLang.text?.toString(),
                    price = null,
                    country = etSearchCountry.text?.toString(),
                    author = etSearchAuthor.text?.toString(),
                    title = etSearchTitle.text?.toString(),
                    minPages = etMinPages.text?.toString()?.toIntOrNull()?:0
                )
                searchRequest()
            }

            updateState(SearchResultsState.SCREEN_OPEN)
        }

        dashboardViewModel.searchBookResultsLiveData.observe(viewLifecycleOwner, ::onBookListResponse)

    }

     fun searchRequest() {
         dashboardAdp.removeAllEntries()
        searchBooksRequest?.let {  dashboardViewModel.searchForBook(it)}
        updateState(SearchResultsState.SEARCHING_FOR_DATA)
    }

    private fun onBookListResponse(response: BaseResponse<List<BookModel>>?) {

        when (response) {
            is BaseResponse.Success -> {
                if (response.body.isEmpty()) updateState(SearchResultsState.EMPTY_SEARCH_RESULTS)
                else updateState(SearchResultsState.DATA_AVAILABLE)
                dashboardAdp.updateAllEntries(response.body)
            }

            else -> {
                showToastFromFragment(response?.statusMsg ?: "")
                updateState(SearchResultsState.SCREEN_OPEN)
            }
        }
    }


    private fun onBookClick(model: BaseListModel) {
        if (model is BookModel)
            DetailsActivity.startActivityForResult(requireActivity() as AppCompatActivity, model)
    }


    private fun updateState(state: SearchResultsState) {
        withBinding {
            when (state) {
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
                SearchResultsState.EMPTY_SEARCH_RESULTS -> {
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

    enum class SearchResultsState { SEARCHING_FOR_DATA, DATA_AVAILABLE, EMPTY_SEARCH_RESULTS, SCREEN_OPEN }
    class StateUpdateWatcher(private val checkerView: ImageView) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (text.isNullOrBlank()) {
                checkerView.setImageResource(R.drawable.ic_check_circle_unfilled)
            }
            else {
                checkerView.setImageResource(R.drawable.ic_check_circle_filled)
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }
}

