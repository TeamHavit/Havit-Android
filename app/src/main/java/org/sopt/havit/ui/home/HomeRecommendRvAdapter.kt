package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.HomeRecommendData
import org.sopt.havit.databinding.ItemHomeRecommendListBinding

class HomeRecommendRvAdapter :
    RecyclerView.Adapter<HomeRecommendRvAdapter.HomeRecommendViewHolder>() {

    var recommendList = mutableListOf<HomeRecommendData>()

    fun setList(list: List<HomeRecommendData>) {
        recommendList = list as MutableList<HomeRecommendData>
    }

    class HomeRecommendViewHolder(private val binding: ItemHomeRecommendListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeRecommendData) {
            binding.dataHomeRecommend = data
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeRecommendRvAdapter.HomeRecommendViewHolder {
        val binding = ItemHomeRecommendListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeRecommendRvAdapter.HomeRecommendViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeRecommendRvAdapter.HomeRecommendViewHolder,
        position: Int
    ) {
        holder.onBind(recommendList[position])
    }

    override fun getItemCount(): Int = recommendList.size
}