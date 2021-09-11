package work.curioustools.jb_mobile.ui_views

import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.databinding.ItemDashboardBookHorizontalBinding
import work.curioustools.jb_mobile.ui_models.BookModel
import work.curioustools.third_party_network.extensions.loadImageFromInternet
import work.curioustools.curiousutils.core_droidjet.arch.BaseListModel
import work.curioustools.curiousutils.core_droidjet.arch.BaseVHWithVB

class BookHorizontalVH(
    private val binding: ItemDashboardBookHorizontalBinding,
    private val staticIconUrlWithSlash: String,
    private val onClick:(BaseListModel) -> Unit
) : BaseVHWithVB(binding) {

    override fun bindData(model: BaseListModel, payload: Any?) {
        if (model !is BookModel) return

        with(binding){
            this.ivIcon.setOnClickListener { onClick.invoke(model) }
            root.setOnClickListener { onClick.invoke(model) }
            tvTitle.text = model.title
            tvSummary.text = if(model.summary.isNotBlank()) model.summary else root.context.getText(R.string.lorem_3_words)
            ivIcon.loadImageFromInternet(
                url = "$staticIconUrlWithSlash${model.imageQueryUrl}",
                placeholder =    R.drawable.turkey_d_black_n_white,
                error =  R.drawable.turkey_d_black_n_white,
                fallback = R.drawable.turkey_d_black_n_white
            )
        }
    }

}


/*

 "price": "$74",
        "author": "Dante Alighieri",
        "country": "Italy",
        "imageLink": "images/the-divine-comedy.jpg",
        "language": "Italian",
        "link": "https://en.wikipedia.org/wiki/Divine_Comedy\n",
        "pages": 928,
        "title": "The Divine Comedy",
        "year": 1315,
        "id": "a046d8ec"
* */