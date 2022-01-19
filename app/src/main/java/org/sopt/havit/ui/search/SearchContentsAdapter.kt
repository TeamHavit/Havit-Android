package org.sopt.havit.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.ItemContentsSearchBinding

class SearchContentsAdapter :
    RecyclerView.Adapter<SearchContentsAdapter.SearchContentsViewHolder>() {

    private var searchContents = mutableListOf<ContentsSearchResponse.Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchContentsViewHolder {
        val binding =
            ItemContentsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchContentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchContentsViewHolder, position: Int) {
        holder.bind(position, searchContents[position])
    }

    override fun getItemCount(): Int = searchContents.size

    fun setItem(data: List<ContentsSearchResponse.Data>) {
        searchContents = data as MutableList<ContentsSearchResponse.Data>
        notifyDataSetChanged()
    }

    inner class SearchContentsViewHolder(
        val binding: ItemContentsSearchBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(position: Int, data: ContentsSearchResponse.Data) {
            binding.content = data
        }
    }
}