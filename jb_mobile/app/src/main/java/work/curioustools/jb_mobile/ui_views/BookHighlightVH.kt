package work.curioustools.jb_mobile.ui_views

import android.view.View
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.textview.MaterialTextView
import work.curioustools.jb_mobile.databinding.ItemDashboardBookHighlightBinding
import work.curioustools.jb_mobile.ui_models.BookModel
import work.curioustools.curiousutils.core_droidjet.arch.BaseListModel
import work.curioustools.curiousutils.core_droidjet.arch.BaseVHWithVB
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.QuickAdapter
import work.curioustools.third_party_network.extensions.loadImageFromInternet

class BookHighlightVH(
    private val binding: ItemDashboardBookHighlightBinding,
    private val staticIconUrlWithSlash: String,
    private val onClick:(BaseListModel) -> Unit
) : BaseVHWithVB(binding) {

    override fun bindData(model: BaseListModel, payload: Any?) {
        if (model !is BookModel) return

        with(binding){
            this.ivIcon.setOnClickListener { onClick.invoke(model) }
            tvTitle.text =  if(model.title.isNotBlank()) model.title else root.context.getText(R.string.lorem_3_words)
            ivIcon.loadImageFromInternet(
                "$staticIconUrlWithSlash${model.imageQueryUrl}",
                R.drawable.bg_rect_gradient_grey,
                R.drawable.turkey_d_white_n_black,
                R.drawable.turkey_d_white_n_black
            )

            rvTags.apply {
                val tags = model.tags.mapIndexed { index, booksTagModel ->
                    if (index == model.tags.lastIndex) booksTagModel
                    else BookModel.BooksTagModel(booksTagModel.tag + " • ")
                }
                val adp =
                    QuickAdapter(tags, R.layout.item_textview) { v: View, item: BookModel.BooksTagModel ->
                        val tag = v.findViewById<MaterialTextView>(R.id.tvTag)
                        tag.text = item.tag
                    }
                layoutManager = FlexboxLayoutManager(context)
                adapter = adp
                setHasFixedSize(true)
            }

        }
    }

}