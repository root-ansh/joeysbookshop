package work.curioustools.jb_mobile.modules.dashboard.ui_views

import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseListModel
import work.curioustools.jb_mobile.commons.BaseVH
import work.curioustools.jb_mobile.databinding.ItemDashboardBookHorizontalBinding
import work.curioustools.jb_mobile.modules.dashboard.ui_models.BookModel
import work.curioustools.jb_mobile.utils.third_party_libs.loadImageFromInternet

class BookHorizontalVH(
    private val binding: ItemDashboardBookHorizontalBinding,
    private val staticIconUrlWithSlash: String,
    private val onClick:(BaseListModel) -> Unit
) : BaseVH(binding) {

    override fun bindData(model: BaseListModel, payload: Any?) {
        if (model !is BookModel) return

        with(binding){
            this.ivIcon.setOnClickListener { onClick.invoke(model) }
            tvTitle.text = model.title
            tvSummary.text = if(model.summary.isNotBlank()) model.summary else root.context.getText(R.string.lorem_3_words)
            ivIcon.loadImageFromInternet(
                "$staticIconUrlWithSlash${model.imageQueryUrl}",
                R.drawable.bg_rect_gradient_grey,
                R.drawable.turkey,
                R.drawable.turkey
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