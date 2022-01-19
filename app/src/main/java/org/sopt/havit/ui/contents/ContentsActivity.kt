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
    private var id = 0
    private var name = "error"
    private var seen: String = "all"
    private var filter: String = "created_at"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.contentsViewModel = contentsViewModel

        setContentView(binding.root)
        initAdapter()
        setData(seen, filter)
        dataObserve()
        decorationView()
        changeLayout()
        clickBack()
        initSearchSticky()
        clickAddContents()
        setOrderDialog()
        moveSearch()
        clickItemView()
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

    private fun setData(s: String, f: String) {
        id = intent.getIntExtra("categoryId", 0)
        if (id == 0) {
            Log.d("categoryId", "error")
        } else {
            intent.getStringExtra("categoryName")?.let {
                name = it
            }
            contentsViewModel.requestContentsTaken(id, s, f, name)
            Log.d("categoryName", "$name")
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
            SaveFragment().show(supportFragmentManager, "언니 사랑해")
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
            contentsViewModel.requestContentsTaken(id, seen, "created_at", name)
            binding.tvOrder.text = "최신순"
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_past).setOnClickListener {
            contentsViewModel.requestContentsTaken(id, seen, "reverse", name)
            binding.tvOrder.text = "과거순"
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_view).setOnClickListener {
            contentsViewModel.requestContentsTaken(id, seen, "seen_at", name)
            binding.tvOrder.text = "최근 조회순"
            bottomSheetDialog.dismiss()
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

    companion object {
        const val LINEAR_MIN_LAYOUT = 1
        const val GRID_LAYOUT = 2
        const val LINEAR_MAX_LAYOUT = 3
        var layout = 1
    }
}