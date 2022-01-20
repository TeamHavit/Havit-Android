package org.sopt.havit.ui.contents

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityContentsBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity

class ContentsActivity : BaseBindingActivity<ActivityContentsBinding>(R.layout.activity_contents) {
    private lateinit var contentsAdapter: ContentsAdapter
    private val contentsViewModel: ContentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.contentsViewModel = contentsViewModel


        setContentView(binding.root)
        initAdapter()
        setData(OPTION, FILTER)
        dataObserve()
        decorationView()
        changeLayout()
        clickBack()
        initSearchSticky()
        clickAddContents()
        setOrderDialog()
        moveSearch()
        clickItemView()
        setChipOrder()
        setCategoryListDialog()
    }


    private fun initAdapter() {
        contentsAdapter = ContentsAdapter(contentsViewModel)
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

    private fun setData(o: String, f: String) {
        // 레이아웃 초기화
        layout = LINEAR_MIN_LAYOUT
        // 데이터 셋팅
        ID = intent.getIntExtra("categoryId", 0)
        if (ID == 0) {
            Log.d("categoryId", "error")
        } else {
            intent.getStringExtra("categoryName")?.let {
                CATEGORY_NAME = it
            }
            contentsViewModel.requestContentsTaken(ID, o, f, CATEGORY_NAME)
            Log.d("categoryName", "$CATEGORY_NAME")
        }
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
                contentsAdapter.contentsList.clear()
                contentsAdapter.contentsList.addAll(it)
                contentsAdapter.notifyDataSetChanged()
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
            SaveFragment(CATEGORY_NAME).show(supportFragmentManager, "언니 사랑해")
        }
    }

    private fun setOrderDialog() {
        val bottomSheetView = layoutInflater.inflate(R.layout.dialog_contents_order, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        binding.tvOrder.setOnClickListener {
            when (binding.tvOrder.text) {
                "최신순" -> {
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_recent)
                        .setBackgroundColor(Color.parseColor("#f7f6ff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_recent)
                        .setTextColor(Color.parseColor("#8578ff"))
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_past)
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_past)
                        .setTextColor(Color.parseColor("#424247"))
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_view)
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_view)
                        .setTextColor(Color.parseColor("#424247"))
                }
                "과거순" -> {
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_past)
                        .setBackgroundColor(Color.parseColor("#f7f6ff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_past)
                        .setTextColor(Color.parseColor("#8578ff"))
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_recent)
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_recent)
                        .setTextColor(Color.parseColor("#424247"))
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_view)
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_view)
                        .setTextColor(Color.parseColor("#424247"))
                }
                else -> {
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_view)
                        .setBackgroundColor(Color.parseColor("#f7f6ff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_view)
                        .setTextColor(Color.parseColor("#8578ff"))
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_recent)
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_recent)
                        .setTextColor(Color.parseColor("#424247"))
                    bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_past)
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                    bottomSheetView.findViewById<TextView>(R.id.tv_past)
                        .setTextColor(Color.parseColor("#424247"))
                }
            }
            bottomSheetDialog.show()
        }

        bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_recent).setOnClickListener {
            FILTER = "created_at"
            contentsViewModel.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
            binding.tvOrder.text = "최신순"
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_past).setOnClickListener {
            FILTER = "reverse"
            contentsViewModel.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
            binding.tvOrder.text = "과거순"
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_view).setOnClickListener {
            FILTER = "seen_at"
            contentsViewModel.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
            binding.tvOrder.text = "최근 조회순"
            bottomSheetDialog.dismiss()
        }
    }

    private fun setCategoryListDialog(){
        binding.clCategory.setOnClickListener {
            DialogContentsCategoryFragment().show(supportFragmentManager, "categoryList")
        }
    }

    private fun moveSearch() {
        binding.clSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("categoryName", "${contentsViewModel.categoryName}")
            startActivity(intent)
        }
    }

    private fun clickItemView() {
        contentsAdapter.setItemClickListener(object : ContentsAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                val intent = Intent(v.context, WebActivity::class.java)
                contentsViewModel.contentsList.value?.get(position)
                    ?.let { intent.putExtra("url", it.url) }
                startActivity(intent)
            }
        })
    }

    private fun setChipOrder() {
        with(binding) {
            chAll.setOnClickListener {
                OPTION = "all"
                contentsViewModel?.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
            }
            chSeen.setOnClickListener {
                OPTION = "true"
                contentsViewModel?.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
            }
            chUnseen.setOnClickListener {
                OPTION = "false"
                contentsViewModel?.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
            }
            chAlarm.setOnClickListener {
                OPTION = "notified"
                contentsViewModel?.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
            }
        }
    }

    companion object {
        const val LINEAR_MIN_LAYOUT = 1
        const val GRID_LAYOUT = 2
        const val LINEAR_MAX_LAYOUT = 3
        var layout = 1

        var ID = 0
        var CATEGORY_NAME = "error"
        var OPTION: String = "all"
        var FILTER: String = "created_at"
    }
}