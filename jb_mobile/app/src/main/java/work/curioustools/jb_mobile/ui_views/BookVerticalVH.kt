package work.curioustools.jb_mobile.ui_views

import work.curioustools.jb_mobile.databinding.ItemDashboardBookVerticalBinding
import work.curioustools.jb_mobile.ui_models.BookModel
import work.curioustools.curiousutils.core_droidjet.arch.BaseListModel
import work.curioustools.curiousutils.core_droidjet.arch.BaseVHWithVB
import work.curioustools.jb_mobile.R
import work.curioustools.third_party_network.extensions.loadImageFromInternet

class BookVerticalVH(
    private val binding: ItemDashboardBookVerticalBinding,
    private val staticIconUrlWithSlash: String,
    private val onClick:(BaseListModel) -> Unit
) : BaseVHWithVB(binding) {

    override fun bindData(model: BaseListModel, payload: Any?) {
        if (model !is BookModel) return

        with(binding){
            this.ivIcon.setOnClickListener { onClick.invoke(model) }
            root.setOnClickListener { onClick.invoke(model) }
            tvTitle.text =  if(model.title.isNotBlank()) model.title else root.context.getText(R.string.lorem_3_words)
            ivIcon.loadImageFromInternet(
                url = "$staticIconUrlWithSlash${model.imageQueryUrl}",
                placeholder =    R.drawable.turkey_d_black_n_white,
                error =  R.drawable.turkey_d_black_n_white,
                fallback = R.drawable.turkey_d_black_n_white
            )
        }
    }

}

