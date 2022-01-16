package org.sopt.havit.ui.contents_simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.data.ContentsData
import org.sopt.havit.data.HomeContentsData
import org.sopt.havit.databinding.ItemContentsLinearMinBinding

class ContentsSimpleRvAdapter :
    RecyclerView.Adapter<ContentsSimpleRvAdapter.ContentsSimpleViewHolder>() {

    var contentsList = mutableListOf<ContentsData>()

    class ContentsSimpleViewHolder(private val binding: ItemContentsLinearMinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ContentsData) {
            binding.content = data
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContentsSimpleViewHolder {
        val binding = ItemContentsLinearMinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ContentsSimpleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ContentsSimpleViewHolder,
        position: Int
    ) {
        holder.onBind(contentsList[position])
    }

    override fun getItemCount(): Int = contentsList.size

    fun setList(list: List<ContentsData>) {
        contentsList = list as MutableList<ContentsData>
    }
}