package org.sopt.havit.ui.contents

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsResponse
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.ItemContentsGridBinding
import org.sopt.havit.databinding.ItemContentsLinearMaxBinding
import org.sopt.havit.databinding.ItemContentsLinearMinBinding

class ContentsAdapter(contentsViewModel: ContentsViewModel, fragmentManager: FragmentManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var contentsList = mutableListOf<ContentsResponse.ContentsData>()
    private var mFragmentManager: FragmentManager = fragmentManager
    private lateinit var itemClickListener: OnItemClickListener
    private var viewModel = contentsViewModel
    // setItemClickListener로 설정한 함수 실행
    private lateinit var havitClickListener: OnHavitClickListener

    // 리스너 인터페이스
    interface OnHavitClickListener {
        fun onHavitClick()
    }

    // 외부에서 클릭 시 이벤트 설정
    fun setHavitClickListener(onItemClickListener: OnHavitClickListener) {
        this.havitClickListener = onItemClickListener
    }

    private fun setTime(time: String): String {
        val date = time.substring(0 until 10)
            .replace("-", ". ")
        val hour = time.substring(11 until 13)
        val minute = time.substring(14 until 16)
        return if (hour < "12") {
            "$date 오전 ${hour}:${minute} "
        } else {
            "$date 오후 ${hour}:${minute} "
        }
    }

    inner class LinearMinViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            if (data.notificationTime.isNotEmpty()) {
                data.notificationTime = setTime(data.notificationTime)
            }
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")

            binding.content = data
            if (data.isSeen) {
                Log.d("HavitButtonTest", "1. binding - seen ${data.title}")
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                Log.d("HavitButtonTest", "1. binding - unseen ${data.title}")
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onClick(data: ContentsResponse.ContentsData, pos: Int) {
            binding.ivHavit.setOnClickListener {
                if(!contentsList[pos].isSeen) {
                    havitClickListener.onHavitClick()
                }
                contentsList[pos].isSeen = !contentsList[pos].isSeen
                viewModel.setIsSeen(data.id)
                if (binding.ivHavit.tag == "unseen") {
                    Log.d("HavitButtonTest", "1. click - unseen 1 : ${binding.ivHavit.tag}")
                    binding.ivHavit.tag = "seen"
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                    Log.d("HavitButtonTest", "1. click - unseen 2 : ${binding.ivHavit.tag}")
                } else {
                    Log.d("HavitButtonTest", "1. click - seen 1 : ${binding.ivHavit.tag}")
                    binding.ivHavit.tag = "unseen"
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                    Log.d("HavitButtonTest", "1. click - seen 2 : ${binding.ivHavit.tag}")
                }
            }
            binding.ivSetting.setOnClickListener {
                val dataMore = ContentsSearchResponse.Data(
                    data.createdAt,
                    data.description,
                    data.id,
                    data.image,
                    data.isNotified,
                    data.isSeen,
                    data.notificationTime,
                    data.title,
                    data.url
                )
                ContentsMoreFragment(dataMore).show(mFragmentManager, "setting")
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemContentsGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            if (data.notificationTime.isNotEmpty()) {
                data.notificationTime = setTime(data.notificationTime)
            }
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")
            binding.content = data
            if (data.isSeen) {
                Log.d("HavitButtonTest", "2. binding - seen ${data.title}")
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                Log.d("HavitButtonTest", "2. binding - unseen ${data.title}")
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onClick(data: ContentsResponse.ContentsData, pos: Int) {
            binding.ivHavit.setOnClickListener {
                if(!contentsList[pos].isSeen) {
                    havitClickListener.onHavitClick()
                }
                contentsList[pos].isSeen = !contentsList[pos].isSeen
                viewModel.setIsSeen(data.id)
                if (binding.ivHavit.tag == "unseen") {
                    Log.d("HavitButtonTest", "2. click - unseen 1 : ${binding.ivHavit.tag}")
                    binding.ivHavit.tag = "seen"
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                    Log.d("HavitButtonTest", "2. click - unseen 2 : ${binding.ivHavit.tag}")
                } else {
                    Log.d("HavitButtonTest", "2. click - seen 1 : ${binding.ivHavit.tag}")
                    binding.ivHavit.tag = "unseen"
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                    Log.d("HavitButtonTest", "2. click - seen 2 : ${binding.ivHavit.tag}")
                }
            }
            binding.ivSetting.setOnClickListener {
                val dataMore = ContentsSearchResponse.Data(
                    data.createdAt,
                    data.description,
                    data.id,
                    data.image,
                    data.isNotified,
                    data.isSeen,
                    data.notificationTime,
                    data.title,
                    data.url
                )
                ContentsMoreFragment(dataMore).show(mFragmentManager, "setting")
            }
        }
    }

    inner class LinearMaxViewHolder(private val binding: ItemContentsLinearMaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsResponse.ContentsData) {
            if (data.notificationTime.isNotEmpty()) {
                data.notificationTime = setTime(data.notificationTime)
            }
            data.createdAt = data.createdAt.substring(0 until 10)
                .replace("-", ". ")
            binding.content = data
            if (data.isSeen) {
                Log.d("HavitButtonTest", "3. binding - seen ${data.title}")
                binding.ivHavit.tag = "seen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
            } else {
                Log.d("HavitButtonTest", "3. binding - unseen ${data.title}")
                binding.ivHavit.tag = "unseen"
                binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
            }
        }

        fun onClick(data: ContentsResponse.ContentsData, pos: Int) {
            binding.ivHavit.setOnClickListener {
                if(!contentsList[pos].isSeen) {
                    havitClickListener.onHavitClick()
                }
                contentsList[pos].isSeen = !contentsList[pos].isSeen
                viewModel.setIsSeen(data.id)
                if (binding.ivHavit.tag == "unseen") {
                    Log.d("HavitButtonTest", "3. click - unseen 1 : ${binding.ivHavit.tag}")
                    binding.ivHavit.tag = "seen"
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_read_2)
                    Log.d("HavitButtonTest", "3. click - unseen 2 : ${binding.ivHavit.tag}")
                } else {
                    Log.d("HavitButtonTest", "3. click - seen 1 : ${binding.ivHavit.tag}")
                    binding.ivHavit.tag = "unseen"
                    binding.ivHavit.setImageResource(R.drawable.ic_contents_unread)
                    Log.d("HavitButtonTest", "3. click - unseen 2 : ${binding.ivHavit.tag}")
                }
            }
            binding.ivSetting.setOnClickListener {
                val dataMore = ContentsSearchResponse.Data(
                    data.createdAt,
                    data.description,
                    data.id,
                    data.image,
                    data.isNotified,
                    data.isSeen,
                    data.notificationTime,
                    data.title,
                    data.url
                )
                ContentsMoreFragment(dataMore).show(mFragmentManager, "setting")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentsActivity.LINEAR_MIN_LAYOUT -> {
                val binding = ItemContentsLinearMinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                LinearMinViewHolder(binding)
            }
            ContentsActivity.GRID_LAYOUT -> {
                val binding = ItemContentsGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                GridViewHolder(binding)
            }
            else -> {
                val binding = ItemContentsLinearMaxBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                LinearMaxViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (ContentsActivity.layout) {
            ContentsActivity.LINEAR_MIN_LAYOUT -> {
                (holder as LinearMinViewHolder).onBind(contentsList[position])
                holder.onClick(contentsList[position], position)
            }
            ContentsActivity.GRID_LAYOUT -> {
                (holder as GridViewHolder).onBind(contentsList[position])
                holder.onClick(contentsList[position], position)
            }
            ContentsActivity.LINEAR_MAX_LAYOUT -> {
                (holder as LinearMaxViewHolder).onBind(contentsList[position])
                holder.onClick(contentsList[position], position)
            }
        }
        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onWebClick(it, position)
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onWebClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int = contentsList.size

    override fun getItemViewType(position: Int): Int {
        return ContentsActivity.layout
    }
}