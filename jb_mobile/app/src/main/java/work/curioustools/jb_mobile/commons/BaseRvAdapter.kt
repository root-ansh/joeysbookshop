package work.curioustools.jb_mobile.commons

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

interface BaseListModel

abstract class BaseVH(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bindData(model: BaseListModel, payload: Any? = null)//todo move to package
}


abstract class BaseRvAdapter<VH : BaseVH> : RecyclerView.Adapter<VH>() {
    private val entries = mutableListOf<BaseListModel>()//todo move to package



    abstract fun onBindVH(holder: VH,position: Int,payloads: MutableList<Any>? =null)

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        onBindVH(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindVH(holder, position)
    }

    override fun getItemCount() = entries.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: VH): Boolean {
        return super.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.unregisterAdapterDataObserver(observer)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }


    fun getCurrentEntries(): List<BaseListModel> = entries.toList()


    @SuppressLint("NotifyDataSetChanged")
    fun removeAllEntries() {
        entries.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeEntriesFrom(startingIdx: Int, endingIdx: Int) {
        //note: Both indices are included.
        if (startingIdx !in entries.indices) return
        val subentries = entries.subList(startingIdx,endingIdx+1)
        entries.removeAll(subentries)

        try {
            val count = endingIdx - startingIdx + 1
            notifyItemRangeRemoved(startingIdx, count)
        }
        catch (t:Throwable){
            Log.e("ADP", "adp function crashed! removeEntriesFrom: $startingIdx - $endingIdx , size = ${entries.size}", t)
            notifyDataSetChanged()
        }
    }

    fun updateAllEntries(newEntries:List<BaseListModel>,payload: Any?=null){
        removeAllEntries()
        entries.addAll(newEntries)
        notifyItemRangeChanged(0,entries.size,payload)
    }

    fun updateEntry(pos:Int, newEntry: BaseListModel,payload: Any? = null){
        if(pos !in entries.indices) return
        entries.add(pos,newEntry)
        notifyItemChanged(pos,payload)
    }

    fun addEntry(entry:BaseListModel){
        entries.add(entry)
        notifyItemInserted(entries.size-1)
    }

}