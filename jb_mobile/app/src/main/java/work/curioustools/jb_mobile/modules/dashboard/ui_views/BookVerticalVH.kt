package work.curioustools.jb_mobile.modules.dashboard.ui_views

import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseListModel
import work.curioustools.jb_mobile.commons.BaseVH
import work.curioustools.jb_mobile.databinding.ItemDashboardBookVerticalBinding
import work.curioustools.jb_mobile.modules.dashboard.ui_models.BookModel
import work.curioustools.jb_mobile.utils.third_party_libs.loadImageFromInternet

class BookVerticalVH(
    private val binding: ItemDashboardBookVerticalBinding,
    private val staticIconUrlWithSlash: String,
    private val onClick:(BaseListModel) -> Unit
) : BaseVH(binding) {

    override fun bindData(model: BaseListModel, payload: Any?) {
        if (model !is BookModel) return

        with(binding){
            this.ivIcon.setOnClickListener { onClick.invoke(model) }
            tvTitle.text =  if(model.title.isNotBlank()) model.title else root.context.getText(R.string.lorem_3_words)
            ivIcon.loadImageFromInternet(
                "$staticIconUrlWithSlash${model.imageQueryUrl}",
                R.drawable.bg_rect_gradient_grey,
                R.drawable.turkey,
                R.drawable.turkey
            )
        }
    }

}

