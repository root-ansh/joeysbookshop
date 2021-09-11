package work.curioustools.jb_mobile.commons

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseVH(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bindData(model: BaseListModel, payload: Any? = null)//todo move to package
}