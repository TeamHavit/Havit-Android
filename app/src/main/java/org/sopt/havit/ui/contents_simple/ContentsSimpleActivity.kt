package org.sopt.havit.ui.contents_simple

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.R
import org.sopt.havit.data.ContentsData
import org.sopt.havit.databinding.ActivityContentsSimpleBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class ContentsSimpleActivity :
    BaseBindingActivity<ActivityContentsSimpleBinding>(R.layout.activity_contents_simple) {

    private val contentsViewModel: ContentsSimpleViewModel by viewModels()
    private lateinit var contentsAdapter: ContentsSimpleRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmContents = contentsViewModel

        initAdapter()
        initContents()
        dataObserve()
        decorationView()
    }

    private fun decorationView() {
        binding.rvContents.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun initAdapter() {
        contentsAdapter = ContentsSimpleRvAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun initContents() {
        intent?.let {
            it.getStringExtra("before")?.let { before ->
                if (before == "unseen") {
                    setUnseenContents()
                } else {
                    setRecentSaveContents()
                    Log.d("contents_simple", "setRecentSaveContents")
                }
            }
        }
    }

    private fun setUnseenContents() {
        Log.d("ContentsSimple_before", "unseen")
    }

    private fun setRecentSaveContents() {
        val list = mutableListOf<ContentsData>()
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
        contentsViewModel.requestContentsTaken(list, "봐야하는")
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            binding.lifecycleOwner?.let {
                contentsList.observe(it) { list ->
                    with(binding) {
                        if (list.isEmpty()) {
                            rvContents.visibility = View.GONE
                            clContentsEmpty.visibility = View.VISIBLE
                        } else {
                            rvContents.visibility = View.VISIBLE
                            clContentsEmpty.visibility = View.GONE
                            contentsAdapter.contentsList.addAll(list)
                            contentsAdapter.notifyDataSetChanged()
                        }
                    }
                    Log.d("contents_simple", "dataObserve() list: $list")
                }
            }
            binding.lifecycleOwner?.let {
                topBarName.observe(it) {
                    binding.tvTopbar.text = it
                }
            }
        }
    }
}