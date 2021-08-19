package work.curioustools.jb_mobile.modules.dashboard.ui_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import work.curioustools.jb_mobile.modules.dashboard.domain_usecases.BookListUseCase
import work.curioustools.jb_mobile.modules.dashboard.ui_models.BookModel
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.BaseResponse
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bookListUseCase: BookListUseCase
):ViewModel() {

    fun getBooksList(){
        bookListUseCase.requestForData(Unit)
    }

    val bookListLiveData : LiveData<BaseResponse<List<BookModel>>> =
        Transformations.switchMap(bookListUseCase.liveData){
            val livedata = MutableLiveData<BaseResponse<List<BookModel>>>()

            when(it){
                is BaseResponse.Success -> {
                   val resp =  BaseResponse.Success(
                        (it.body?: listOf()).mapIndexed {index,dto ->
                            BookModel(
                                price = dto.price ?: "",
                                author = dto.author ?: "",
                                country = dto.country ?: "",
                                imageQueryUrl = dto.imageLink ?: "",
                                wiki = dto.link ?: "",
                                pages = dto.pages ?: 0,
                                title = dto.title ?: "",
                                summary = dto.summary ?: "",
                                year = dto.year ?: 1900,
                                id = dto.id ?: "",
                                tags = (dto.tags ?: listOf()).map { BookModel.BooksTagModel(it) },
                                uiType = if ((index + 1) % 3 == 0) BookModel.BookUiType.GRID else BookModel.BookUiType.HORIZONTAL
                            )
                        }
                    )
                    livedata.postValue(resp)

                }

                is BaseResponse.Failure ->livedata.postValue( BaseResponse.Failure(null,it.statusCode))

            }

            livedata

        }
}