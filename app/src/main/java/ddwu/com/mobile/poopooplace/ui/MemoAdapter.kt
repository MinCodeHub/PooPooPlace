package ddwu.com.mobile.poopooplace.ui.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.poopooplace.data.MemoDto
import ddwu.com.mobile.poopooplace.databinding.ReviewItemBinding

class MemoAdapter: RecyclerView.Adapter<MemoAdapter.MemoHolder>(){

    var memoList: List<MemoDto>? = null
    var itemClickListener: OnMemoItemClickListener? = null

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

    class MemoHolder(val itemBinding: ReviewItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnMemoItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnMemoItemClickListener) {
        itemClickListener = listener
    }


}