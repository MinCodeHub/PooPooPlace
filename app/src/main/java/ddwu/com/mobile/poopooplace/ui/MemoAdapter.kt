package ddwu.com.mobile.poopooplace.ui.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.poopooplace.data.MemoDto
import ddwu.com.mobile.poopooplace.databinding.ReviewItemBinding

class MemoAdapter: RecyclerView.Adapter<MemoAdapter.MemoHolder>(){

    var memoList: List<MemoDto>? = null
    var itemClickListener: OnMemoItemClickListener? = null
    var itemLongClickListener: OnMemoItemLongClickListener? = null

    override fun getItemCount(): Int {
        return memoList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        val itemBinding = ReviewItemBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return MemoHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        val dto = memoList?.get(position)
        holder.itemBinding.tvData.text = dto?.toString()
        holder.itemBinding.clItem.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    inner class MemoHolder(val itemBinding: ReviewItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            // Set up long-click listener here
            itemBinding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemLongClickListener?.onItemLongClick(position)
                }
                true
            }
        }
    }
    interface OnMemoItemClickListener {
        fun onItemClick(position: Int)

    }
    interface OnMemoItemLongClickListener{
        fun onItemLongClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnMemoItemClickListener) {
        itemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnMemoItemLongClickListener) {
        itemLongClickListener = listener
    }


}