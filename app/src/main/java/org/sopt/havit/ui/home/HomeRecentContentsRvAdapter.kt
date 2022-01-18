package org.sopt.havit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.databinding.ItemHomeRecentContentsListBinding

class HomeRecentContentsRvAdapter :
    RecyclerView.Adapter<HomeRecentContentsRvAdapter.HomeContentsViewHolder>() {

    var contentsList = mutableListOf<ContentsSimpleResponse.ContentsSimpleData>()

    class HomeContentsViewHolder(private val binding: ItemHomeRecentContentsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsSimpleResponse.ContentsSimpleData) {
            data.description = data.description.replace(" ", "\u00a0") // tvHeader 단어 자동줄바꿈 막는 코드
            binding.dataHomeContents = data

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeContentsViewHolder {
        val binding = ItemHomeRecentContentsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return HomeContentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeContentsViewHolder, position: Int) {
        holder.onBind(contentsList[position])
    }

//    fun setList(list: List<ContentsSimpleResponse.ContentsSimpleData>) {
//        contentsList = list as MutableList<ContentsSimpleResponse.ContentsSimpleData>
//    }

    override fun getItemCount(): Int = contentsList.size
}