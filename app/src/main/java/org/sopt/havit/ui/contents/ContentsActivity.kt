package org.sopt.havit.ui.contents

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.ActivityContentsBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.category.CategoryOrderModifyActivity
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity

class ContentsActivity : BaseBindingActivity<ActivityContentsBinding>(R.layout.activity_contents) {
    private lateinit var contentsAdapter: ContentsAdapter
    private val contentsViewModel: ContentsViewModel by lazy { ContentsViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.contentsViewModel = contentsViewModel

        setContentView(binding.root)

        initData()
        initAdapter()
        setData()
        dataObserve()
        changeLayout()
        clickBack()
        initSearchSticky()
        clickAddContents()
        setOrderDialog()
        moveSearch()
        clickItemView()
        setChipOrder()
        setCategoryListDialog()
        clickModify()
        clickItemHavit()
        clickItemSetting()
    }

    override fun onStart() {
        super.onStart()
        setContentsData()
    }

    private fun setContentsData() {
        if (ID == -1) {
            contentsViewModel.requestContentsAllTaken(OPTION, FILTER, CATEGORY_NAME)
        } else {
            contentsViewModel.requestContentsTaken(ID, OPTION, FILTER, CATEGORY_NAME)
        }
    }

    private fun initAdapter() {
        contentsAdapter = ContentsAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun initData() {
        // 레이아웃 초기화
        layout = LINEAR_MIN_LAYOUT
        // 옵션 및 필터 초기화
        OPTION = "all"
        FILTER = "created_at"
    }

    private fun setData() {
        ID = intent.getIntExtra("categoryId", 0)
        if (ID == -1) {
            binding.tvModify.visibility = View.GONE
            binding.ivCategoryDrop.visibility = View.GONE
        }
        intent.getStringExtra("categoryName")?.let {
            CATEGORY_NAME = it
        }
        Log.d("categoryName", "$CATEGORY_NAME")
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            contentsList.observe(this@ContentsActivity) {
                with(binding) {
                    if (it.isEmpty()) {
                        Log.d("visibility", " success")
                        rvContents.visibility = View.GONE
                        clEmpty.visibility = View.VISIBLE
                    } else {
                        rvContents.visibility = View.VISIBLE
                        clEmpty.visibility = View.GONE
                        Log.d("visibility", " fail")
                    }
                }
                // 콘텐츠 데이터 업데이트
                contentsAdapter.submitList(it.toList())
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

    // 최신순, 과거순, 최근 조회순 다이얼로그별 화면 설정
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
            binding.tvOrder.text = "최신순"
            setContentsData()
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_past).setOnClickListener {
            FILTER = "reverse"
            binding.tvOrder.text = "과거순"
            setContentsData()
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<ConstraintLayout>(R.id.cl_view).setOnClickListener {
            FILTER = "seen_at"
            binding.tvOrder.text = "최근 조회순"
            setContentsData()
            bottomSheetDialog.dismiss()
        }
    }

    private fun setCategoryListDialog() {
        binding.clCategory.setOnClickListener {
            binding.ivCategoryDrop.setImageResource(R.drawable.ic_dropback_black)
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
                    ?.let {
                        intent.putExtra("url", it.url)
                        intent.putExtra("contentsId", it.id)
                        intent.putExtra("isSeen", it.isSeen)
                    }
                startActivity(intent)
            }
        })
    }

    // 콘텐츠 더보기 클릭 시 이벤트
    private fun clickItemSetting(){
        contentsAdapter.setItemSetClickListner(object: ContentsAdapter.OnItemSetClickListener{
            override fun onSetClick(v: View, position: Int) {
                val dataMore = contentsViewModel.contentsList.value?.get(position)!!.let {
                    ContentsSearchResponse.Data(
                        it.createdAt,
                        it.description,
                        it.id,
                        it.image,
                        it.isNotified,
                        it.isSeen,
                        it.notificationTime,
                        it.title,
                        it.url
                    )
                }
                val dialog = ContentsMoreFragment(dataMore)
                dialog.setClickListener(object: ContentsMoreFragment.OnClickListener{
                    override fun onUpdate() {
                        // 내용 변경 시 서버에서 데이터를 다시 불러온다
                        setContentsData()
                        dialog.dismiss()
                    }
                })
                dialog.show(supportFragmentManager, "setting")
            }
        })
    }

    private fun setChipOrder() {
        with(binding) {
            chAll.setOnClickListener {
                OPTION = "all"
                setContentsData()
            }
            chSeen.setOnClickListener {
                OPTION = "true"
                setContentsData()
            }
            chUnseen.setOnClickListener {
                OPTION = "false"
                setContentsData()
            }
            chAlarm.setOnClickListener {
                OPTION = "notified"
                setContentsData()
            }
        }
    }

    private fun clickModify(){
        binding.tvModify.setOnClickListener {
            val intent = Intent(this, CategoryOrderModifyActivity::class.java)
            intent.putExtra("dataSet", true)
            startActivity(intent)
        }
    }

    private fun setCustomToast() {
        val toast = Toast(this)
        val view = layoutInflater.inflate(R.layout.toast_havit_complete, null)
        toast.view = view
        toast.show()
    }

    // 해빗 클릭 시 이벤트 함수 정의
    private fun clickItemHavit(){
        contentsAdapter.setHavitClickListener(object : ContentsAdapter.OnItemHavitClickListener {
            override fun onHavitClick(v: ImageView, position: Int) {
                with(contentsAdapter) {
                    // 보지 않았던 콘텐츠의 경우 콘텐츠를 봤다는 토스트 띄우기
                    if (!currentList[position].isSeen) {
                        setCustomToast()
                    }

                    currentList[position].isSeen = !currentList[position].isSeen
                    // 서버 호출
                    contentsViewModel.setIsSeen(currentList[position].id)
                    // 태그 바꾸기
                    when(v.tag){
                        "unseen" -> {
                            v.tag = "seen"
                            v.setImageResource(R.drawable.ic_contents_read_2)
                        }
                        else -> {
                            v.tag = "unseen"
                            v.setImageResource(R.drawable.ic_contents_unread)
                        }
                    }
                }
            }
        })
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