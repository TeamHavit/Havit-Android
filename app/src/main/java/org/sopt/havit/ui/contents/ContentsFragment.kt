package org.sopt.havit.ui.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.R
import org.sopt.havit.data.ContentsData
import org.sopt.havit.databinding.FragmentContentsBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class ContentsFragment : BaseBindingFragment<FragmentContentsBinding>(R.layout.fragment_contents) {
    private var _contentsAdapter: ContentsAdapter? = null
    private val contentsAdapter get() = _contentsAdapter ?: error("adapter error")

    private val contentsViewModel: ContentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.contentsViewModel = contentsViewModel

        initAdapter()
        decorationView()
        setData()
        dataObserve()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _contentsAdapter = null
    }

    private fun initAdapter() {
        _contentsAdapter = ContentsAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun decorationView() {
        binding.rvContents.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setData() {
        val list = mutableListOf<ContentsData>()
        val category = "카테고리명"
        for (i in 1..15) {
            list.add(
                ContentsData(
                    true,
                    "카테고리명",
                    "슈슈슉 이것은 제목입니다 슈슉슉슉 이것은 제목입니다 슈슉슉슉 이것은 제목입니다 슈슉",
                    "상세내용 상세내용 상세내용 상세내용 상세내용 상세내용 상세내용은 한줄만만",
                    true,
                    "2021. 11. 24",
                    "https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg",
                    "2021. 11. 17 오전 12:30 ",
                    "www.brunch.com.dididididididididididididi"
                )
            )
        }
        contentsViewModel.requestContentsTaken(list, category, list.size.toString(), "최근순")
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            contentsList.observe(viewLifecycleOwner) {
                contentsAdapter.contentsList.addAll(it)
                contentsAdapter.notifyDataSetChanged()
            }
            categoryName.observe(viewLifecycleOwner) {
                binding.tvCategory.text = it
            }
            contentsCount.observe(viewLifecycleOwner) {
                binding.tvContentsCount.text = it
            }
        }
    }
}