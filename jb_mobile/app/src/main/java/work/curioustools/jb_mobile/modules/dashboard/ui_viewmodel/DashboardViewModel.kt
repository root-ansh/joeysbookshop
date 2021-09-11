package work.curioustools.jb_mobile.modules.dashboard.ui_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import work.curioustools.jb_mobile.modules.dashboard.data_apis.DeleteBookRequest
import work.curioustools.jb_mobile.modules.dashboard.data_apis.SearchBooksRequest
import work.curioustools.jb_mobile.modules.dashboard.domain_usecases.BookListUseCase
import work.curioustools.jb_mobile.modules.dashboard.domain_usecases.DeleteBookUseCase
import work.curioustools.jb_mobile.modules.dashboard.domain_usecases.SearchBooksUseCase
import work.curioustools.jb_mobile.modules.dashboard.domain_usecases.UpdateBookUseCase
import work.curioustools.jb_mobile.modules.dashboard.ui_models.BookModel
import work.curioustools.jb_mobile.modules.dashboard.ui_models.toDto
import work.curioustools.jb_mobile.modules.dashboard.ui_models.toModel
import work.curioustools.third_party_network.arch_network.BaseResponse
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bookListUseCase: BookListUseCase,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase,
) : ViewModel() {

    fun getBooksList() {
        bookListUseCase.requestForData(Unit)
    }

    val bookListLiveData: LiveData<BaseResponse<List<BookModel>>> =
        Transformations.switchMap(bookListUseCase.liveData) {
            val livedata = MutableLiveData<BaseResponse<List<BookModel>>>()

            when (it) {
                is BaseResponse.Success -> {
                    val resp = BaseResponse.Success(
                        (it.body ?: listOf()).mapIndexed { index, dto ->
                            dto.toModel().also { model ->
                                model.uiType = if ((index + 1) % 7 == 0)
                                    BookModel.BookUiType.HORIZONTAL else BookModel.BookUiType.GRID
                            }
                        }
                    )
                    livedata.postValue(resp)
                }

                is BaseResponse.Failure -> livedata.postValue(
                    BaseResponse.Failure(
                        null,
                        it.statusCode
                    )
                )

            }

            livedata

        }


    val searchBookResultsLiveData: LiveData<BaseResponse<List<BookModel>>> =
        Transformations.switchMap(searchBooksUseCase.liveData) {
            val livedata = MutableLiveData<BaseResponse<List<BookModel>>>()

            when (it) {
                is BaseResponse.Success -> {
                    val resp = BaseResponse.Success(
                        (it.body ?: listOf()).mapIndexed { index, dto ->
                           val model = dto.toModel()
                            model.uiType = BookModel.BookUiType.HORIZONTAL
                            model
                        }
                    )
                    livedata.postValue(resp)

                }

                is BaseResponse.Failure -> livedata.postValue(
                    BaseResponse.Failure(
                        null,
                        it.statusCode
                    )
                )

            }

            livedata

        }


    fun searchForBook(searchBooksRequest: SearchBooksRequest){
        searchBooksUseCase.requestForData(searchBooksRequest)
    }

    val updateBookStatusLiveData: LiveData<BaseResponse<Boolean>> =
        Transformations.switchMap(updateBookUseCase.liveData) {
            val livedata = MutableLiveData<BaseResponse<Boolean>>()

            when (it) {
                is BaseResponse.Success -> {
                    val resp = BaseResponse.Success(
                        it.body?.success?:false
                    )
                    livedata.postValue(resp)

                }

                is BaseResponse.Failure -> livedata.postValue(
                    BaseResponse.Failure(
                        null,
                        it.statusCode
                    )
                )

            }

            livedata

        }

    fun updateBook(bookRequest:BookModel){
        updateBookUseCase.requestForData(bookRequest.toDto())
    }

    val deleteBookLiveData: LiveData<BaseResponse<Boolean>> =
        Transformations.switchMap(deleteBookUseCase.liveData) {
            val livedata = MutableLiveData<BaseResponse<Boolean>>()

            when (it) {
                is BaseResponse.Success -> {
                    val resp = BaseResponse.Success(
                        it.body?.success?:false
                    )
                    livedata.postValue(resp)

                }

                is BaseResponse.Failure -> livedata.postValue(
                    BaseResponse.Failure(
                        null,
                        it.statusCode
                    )
                )

            }

            livedata

        }

    fun deleteBook(id:String){
        deleteBookUseCase.requestForData(DeleteBookRequest(id))
    }







}