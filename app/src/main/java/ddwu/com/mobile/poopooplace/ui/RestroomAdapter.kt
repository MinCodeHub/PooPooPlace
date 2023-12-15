package ddwu.com.mobile.poopooplace.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.poopooplace.data.Restroom
import ddwu.com.mobile.poopooplace.databinding.FragmentMyReviewsBinding
import ddwu.com.mobile.poopooplace.databinding.ListItemBinding
interface OnItemClickListener {
    fun onItemClick(restroom: Restroom) // YourRestroomModel을 실제 사용하는 모델로 대체
}
class RestroomAdapter : RecyclerView.Adapter<RestroomAdapter.RestRoomHolder>() {
    var restrooms: List<Restroom>? = null
    var onItemClickListener: OnItemClickListener? = null

    override fun getItemCount(): Int {
        return restrooms?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestRoomHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemBinding.inflate(inflater, parent, false)
        return RestRoomHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RestRoomHolder, position: Int) {
        holder.itemBinding.tvItem.text = restrooms?.get(position).toString()
    }


    inner class RestRoomHolder(val itemBinding: ListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
    , View.OnClickListener{
        init {
            itemBinding.root.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(restrooms!![position])
            }
        }
    }

}