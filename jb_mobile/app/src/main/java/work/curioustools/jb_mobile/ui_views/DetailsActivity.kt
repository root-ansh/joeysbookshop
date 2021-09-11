package work.curioustools.jb_mobile.ui_views

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import work.curioustools.core_jdk.toIntSafe
import work.curioustools.curiousutils.core_droidjet.extensions.*
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseHiltActivityVB
import work.curioustools.jb_mobile.databinding.ActivityDetailsBinding
import work.curioustools.jb_mobile.data_apis.DashboardApi
import work.curioustools.jb_mobile.ui_models.BookModel
import work.curioustools.jb_mobile.ui_viewmodel.DashboardViewModel
import work.curioustools.third_party_network.arch_network.BaseResponse
import work.curioustools.third_party_network.extensions.loadImageFromInternet

class DetailsActivity : BaseHiltActivityVB<ActivityDetailsBinding>() {

    private var isEditing = false

    private  var book: BookModel? = null
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun getBindingForComponent(layoutInflater: LayoutInflater)=  ActivityDetailsBinding.inflate(layoutInflater)

    override fun setup()  {
        setSystemBottomNavBarColor(R.color.black)           //todo systemSetColorForBottomNavBar()
        toggleActionBar(false)                        //todo systemToggleThemeProvidedActionBar()
        setStatusBarIconColorAsWhite(isDarkThemeOn())       //todo systemSetStatusBarIconsAsWhite()
        setStatusBarColor(R.color.black)

         book = intent.getParcelableExtra(DetailsActivity.BOOK)
        book?.let {
            withBinding {
                etTitle.setText(it.title)
                etAuthor.setText(it.author)
                etCountry.setText(it.country)
                etLanguage.setText(it.language)
                etPages.setText(it.pages.toString())
                etPrice.setText(it.price)
                etYear.setText(it.year.toString())
                etSummary.setText(it.summary)
                etWiki.apply {
                    setText(it.wiki)
                    setOnClickListener { _ ->
                        if (it.wiki.isNotBlank()) {
                            CustomTabsIntent.Builder().apply {
                                build().launchUrl(this@DetailsActivity, Uri.parse(it.wiki))

                            }
                        }

                    }
                }
                ivBookImage.loadImageFromInternet(
                    url = "${DashboardApi.BASE_URL}${it.imageQueryUrl}",
                    placeholder = R.drawable.turkey_d_black_n_white,
                    error = R.drawable.turkey_d_black_n_white,
                    fallback = R.drawable.turkey_d_black_n_white
                )
                btEditOrUpdate.setOnClickListener {
                    if (!isEditing) {
                        toggleScreen(true)
                        etPages.showKeyboardForced(this@DetailsActivity)
                    }
                    else {
                        toggleScreen(false)
                        val getDataFromScreen: BookModel = getBookData()
                        dashboardViewModel.updateBook(getDataFromScreen)
                        showToast("updating")
                    }
                }
                btDelete.setOnClickListener {
                   book?.let { dashboardViewModel.deleteBook(it.id) }
                    showToast("deleting")
                }
            }
        }

        dashboardViewModel.updateBookStatusLiveData.observe(this){
            if(it is BaseResponse.Success && it.body){
                showToast("update successfully")
            }
            else showToast("something went wrong, try agaon later")
            setResult(AppCompatActivity.RESULT_OK)
            finishDelayed(100)
        }
        dashboardViewModel.deleteBookLiveData.observe(this){
            if(it is BaseResponse.Success && it.body){
                showToast("delete successfully")
            }
            else showToast("something went wrong, try agaon later")

            setResult(AppCompatActivity.RESULT_OK)
            finishDelayed(100)
        }
        toggleScreen(false)
    }

    private fun getBookData(): BookModel {
        return letBinding {
            BookModel(
                price =etPrice.text.toString(),
                author = etAuthor.text.toString(),
                country = etCountry.text.toString(),
                imageQueryUrl = book?.imageQueryUrl?:"",
                wiki = etWiki.text.toString(),
                pages = etPages.text.toString().toIntOrNull()?:0,
                title = etTitle.text.toString(),
                summary = etSummary.text.toString(),
                year = etYear.text.toString().toIntSafe(),
                id = book?.id?:"",
                language = etLanguage.text.toString(),
                tags = book?.tags?: listOf(),
            )
        }


    }

    private fun toggleScreen(enable: Boolean) {
        isEditing = enable
        withBinding {
            etAuthor.isEnabled = enable
            etTitle.isEnabled = enable
            etCountry.isEnabled = enable
            etLanguage.isEnabled = enable
            etPages.isEnabled = enable
            etPrice.isEnabled = enable
            etYear.isEnabled = enable
            etSummary.isEnabled = enable
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                etWiki.focusable = if(enable) EditText.FOCUSABLE else EditText.NOT_FOCUSABLE
            }


            if(enable){
                btEditOrUpdate.text = "update book details"
            }
            else{
                btEditOrUpdate.text = "edit this book"
            }
        }

    }



    companion object {
        const val CLOSING_WITH_UPDATE = 23
        private const val BOOK = "BOOK"

        fun startActivityForResult(act: AppCompatActivity, book: BookModel, rc:Int= CLOSING_WITH_UPDATE) {
            Intent(act, DetailsActivity::class.java).let {
                it.putExtra(BOOK, book)
                it.startThisActivityForResult(act,rc)
            }
        }
    }
}