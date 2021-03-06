package work.curioustools.jb_mobile.ui_adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import work.curioustools.curiousutils.core_droidjet.extensions.rvInflater
import work.curioustools.jb_mobile.databinding.ItemDashboardBookHorizontalBinding
import work.curioustools.jb_mobile.databinding.ItemDashboardBookVerticalBinding
import work.curioustools.jb_mobile.ui_models.BookModel
import work.curioustools.jb_mobile.ui_views.BookHorizontalVH
import work.curioustools.jb_mobile.ui_views.BookVerticalVH

import work.curioustools.curiousutils.core_droidjet.arch.BaseListModel
import work.curioustools.curiousutils.core_droidjet.arch.BaseRvAdapter
import work.curioustools.curiousutils.core_droidjet.arch.BaseVHWithVB


class AllBooksAdapter(
    private val staticIconUrlWithSlash:String,
    private val onClick: (BaseListModel) -> Unit
)
    : BaseRvAdapter<BaseVHWithVB>() { //todo baseVH me binddata, base vh with vb me nahi

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVHWithVB {
        val uiType = BookModel.BookUiType.getTypeByID(viewType)
        val inflater = parent.rvInflater()
        return when(uiType){
            BookModel.BookUiType.GRID -> BookVerticalVH(
                ItemDashboardBookVerticalBinding.inflate(inflater,null,false),
                staticIconUrlWithSlash , onClick
            )
            BookModel.BookUiType.HORIZONTAL ,BookModel.BookUiType.HIGHLIGHT-> {
                BookHorizontalVH(
                    ItemDashboardBookHorizontalBinding.inflate(inflater,null,false),
                    staticIconUrlWithSlash , onClick
                )
            }
        }
    }


    override fun onBindVH(holder: BaseVHWithVB, position: Int, payloads: MutableList<Any>?) {
       holder.bindData(getCurrentEntries()[position],payloads)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getCurrentEntries()[position] as? BookModel
        return item?.uiType?.id ?: BookModel.BookUiType.default().id
    }

    companion object {
        private const val GRID_MAX_COLS_PER_ROW = 2
        fun getLayoutManager(ctx: Context, adp:AllBooksAdapter) :GridLayoutManager{
            val layoutManager = GridLayoutManager(ctx, GRID_MAX_COLS_PER_ROW)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {

                    return when (adp.getItemViewType(position)) {
                        BookModel.BookUiType.HORIZONTAL.id -> GRID_MAX_COLS_PER_ROW
                        BookModel.BookUiType.GRID.id -> GRID_MAX_COLS_PER_ROW/GRID_MAX_COLS_PER_ROW
                        else -> GRID_MAX_COLS_PER_ROW
                    }
                }

            }
            return layoutManager
        }

    }

}