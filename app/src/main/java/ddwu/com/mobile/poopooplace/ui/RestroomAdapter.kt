package ddwu.com.mobile.poopooplace.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.poopooplace.data.Restroom
import ddwu.com.mobile.poopooplace.databinding.FragmentMyReviewsBinding

class RestroomAdapter : RecyclerView.Adapter<RestroomAdapter.MovieHolder>() {
    var restrooms: List<Restroom>? = null

    override fun getItemCount(): Int {
        return restrooms?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemBinding = FragmentMyReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
//        holder.itemBinding.tvItem.text =
    }

    class MovieHolder(val itemBinding: FragmentMyReviewsBinding) : RecyclerView.ViewHolder(itemBinding.root)
}