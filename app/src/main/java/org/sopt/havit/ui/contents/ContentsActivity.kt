package org.sopt.havit.ui.contents

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityContentsBinding
import org.sopt.havit.ui.save.SaveFragment

class ContentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentsBinding
    private lateinit var contentsAdapter: ContentsAdapter

    private val contentsViewModel: ContentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContentsBinding.inflate(layoutInflater)

        //contentsViewModel.init()
        binding.contentsViewModel = contentsViewModel

        setContentView(binding.root)
        initAdapter()
        setData()
        dataObserve()
        decorationView()
        changeLayout()
        clickBack()
        initSearchSticky()
        clickAddContents()
    }

    private fun initAdapter() {
        contentsAdapter = ContentsAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun decorationView() {
        binding.rvContents.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setData() {
        val list = mutableListOf<ContentsData>()
        val category = "카테고리명"
//        for (i in 1..15) {
//            list.add(
//                ContentsData(
//                    true,
//                    "카테고리명",
//                    "슈슈슉 이것은 제목입니다 슈슉슉슉 이것은 제목입니다 슈슉슉슉 이것은 제목입니다 슈슉",
//                    "상세내용 상세내용 상세내용 상세내용 상세내용 상세내용 상세내용은 한줄만만",
//                    true,
//                    "2021. 11. 24",
//                    "https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg",
//                    "2021. 11. 17 오전 12:30 ",
//                    "www.brunch.com.dididididididididididididi"
//                )
//            )
//        }
        contentsViewModel.requestContentsTaken(list, category, list.size.toString(), "최근순")
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            contentsList.observe(this@ContentsActivity) {
                with(binding) {
                    if (it.isEmpty()) {
                        Log.d("count ", "${contentsAdapter.contentsList.size}")
                        Log.d("visibility", " success")
                        rvContents.visibility = View.GONE
                        clEmpty.visibility = View.VISIBLE
                    } else {
                        rvContents.visibility = View.VISIBLE
                        clEmpty.visibility = View.GONE
                        Log.d("count ", "${contentsAdapter.contentsList.size}")
                        Log.d("visibility", " fail")
                    }
                }
                contentsAdapter.contentsList.addAll(it)
                contentsAdapter.notifyDataSetChanged()
            }
            categoryName.observe(this@ContentsActivity) {
                binding.tvCategory.text = it
            }
            contentsCount.observe(this@ContentsActivity) {
                binding.tvContentsCount.text = it
            }
        }
    }

    private fun changeLayout() {
        binding.ivLayout.setOnClickListener {
            when (layout) {
                LINEAR_MIN_LAYOUT -> {
                    layout = GRID_LAYOUT
                    binding.rvContents.layoutManager = GridLayoutManager(this@ContentsActivity, 2)
                    binding.ivLayout.setImageResource(R.drawable.ic_layout_4)
                }
                GRID_LAYOUT -> {
                    layout = LINEAR_MAX_LAYOUT
                    binding.rvContents.layoutManager = LinearLayoutManager(this@ContentsActivity)
                    binding.ivLayout.setImageResource(R.drawable.ic_layout_2)
                }
                LINEAR_MAX_LAYOUT -> {
                    layout = LINEAR_MIN_LAYOUT
                    binding.rvContents.layoutManager = LinearLayoutManager(this@ContentsActivity)
                    binding.ivLayout.setImageResource(R.drawable.ic_layout_3)
                }
            }
        }
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun initSearchSticky() {
        binding.svMain.run {
            header = binding.clOrderOption
            stickListener = { _ ->
                Log.d("LOGGER_TAG", "stickListener")
            }
            freeListener = { _ ->
                Log.d("LOGGER_TAG", "freeListener")
            }
        }
    }

    private fun clickAddContents() {
        binding.tvAddContents.setOnClickListener {
            SaveFragment().show(supportFragmentManager, "언니 사랑해")
        }
    }

    companion object {
        const val LINEAR_MIN_LAYOUT = 1
        const val GRID_LAYOUT = 2
        const val LINEAR_MAX_LAYOUT = 3
        var layout = 1
    }
}