package org.sopt.havit.ui.home

import android.util.Log
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
            if (data.createdAt.isNotEmpty()) {
            val time = data.createdAt.substring(0 until 10)
            data.createdAt = time.replace("-", ". ")
                Log.d("HomeRecentContentsRvAdapter TIME", data.createdAt.length.toString())
            }
            Log.d("HomeRecentContentsRvAdapter TIME", data.notificationTime)
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

    override fun getItemCount(): Int = contentsList.size
}