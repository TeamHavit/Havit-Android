package org.sopt.havit.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.ItemContentsSearchBinding
import org.sopt.havit.ui.contents.ContentsMoreFragment
import org.sopt.havit.ui.contents_simple.ContentsSimpleRvAdapter
import org.sopt.havit.ui.web.WebActivity

class SearchContentsAdapter(searchViewModel: SearchViewModel, fragmentManager: FragmentManager) :
    RecyclerView.Adapter<SearchContentsAdapter.SearchContentsViewHolder>() {

    private var searchContents = mutableListOf<ContentsSearchResponse.Data>()
    private var isRead = false
    private var viewModel = searchViewModel

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchContentsViewHolder {
        val binding =
            ItemContentsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.vm = viewModel
        return SearchContentsViewHolder(binding)
    }

    private var mFragmentManager: FragmentManager = fragmentManager


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
            contentsHavitClick(position, data)
            isRead = data.isSeen
        }

        fun contentsHavitClick(position: Int, data: ContentsSearchResponse.Data) {
            binding.ivHavit.setOnClickListener {
                if (it.tag == "seen") {
                    Glide.with(binding.ivHavit.context)
                        .load(R.drawable.ic_contents_unread)
                        .into(binding.ivHavit)
                    it.tag = "unseen"
                    viewModel.setIsSeen(data.id)
                    viewModel.setHavitToast(false)
                    isRead= false
                } else {
                    Glide.with(binding.ivHavit.context)
                        .load(R.drawable.ic_contents_read_2)
                        .into(binding.ivHavit)
                    it.tag = "seen"
                    viewModel.setIsSeen(data.id)
                    viewModel.setHavitToast(true)
                    isRead= true
                }

            }
            binding.clSearchItem.setOnClickListener {
                isRead = data.isSeen
                var intent = Intent(it.context, WebActivity::class.java)
                intent.putExtra("url", data.url)
                intent.putExtra("isSeen",isRead)
                intent.putExtra("contentsId",data.id)
                it.context.startActivity(intent)
            }
            binding.ivSetting.setOnClickListener {
                ContentsMoreFragment(data).show(mFragmentManager, "setting")
            }
        }
    }
}