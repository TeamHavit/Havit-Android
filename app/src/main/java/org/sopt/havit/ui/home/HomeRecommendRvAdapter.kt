package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.RecommendationResponse
import org.sopt.havit.databinding.ItemHomeRecommendListBinding

class HomeRecommendRvAdapter :
    RecyclerView.Adapter<HomeRecommendRvAdapter.HomeRecommendViewHolder>() {

    var recommendList = mutableListOf<RecommendationResponse.RecommendationData>()
    private lateinit var itemClickListener: OnItemClickListener

    class HomeRecommendViewHolder(private val binding: ItemHomeRecommendListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: RecommendationResponse.RecommendationData) {
            binding.recommendationData = data
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeRecommendViewHolder {
        val binding = ItemHomeRecommendListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeRecommendViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeRecommendViewHolder,
        position: Int
    ) {
        holder.onBind(recommendList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onWebClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onWebClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = recommendList.size
}