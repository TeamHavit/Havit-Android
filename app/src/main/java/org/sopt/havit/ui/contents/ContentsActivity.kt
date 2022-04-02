package org.sopt.havit.ui.contents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.ActivityContentsBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.category.CategoryOrderModifyActivity
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.CustomToast

class ContentsActivity : BaseBindingActivity<ActivityContentsBinding>(R.layout.activity_contents) {
    private lateinit var contentsAdapter: ContentsAdapter
    private val contentsViewModel: ContentsViewModel by lazy { ContentsViewModel(this) }
    private val categoryList = ArrayList<CategoryResponse.AllCategoryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.contentsViewModel = contentsViewModel

        setContentView(binding.root)

        setCategoryInfo()
        initValue()
        initAdapter()
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
        clickItemMore()
    }

    override fun onStart() {
        super.onStart()
        deletedCategory()
        setContentsData()
        setCategoryName()
    }

    private fun setContentsData() {
        if (categoryId <= -1) {
            contentsViewModel.requestContentsAllTaken(contentsOption, contentsFilter, categoryName)
        } else {
            contentsViewModel.requestContentsTaken(categoryId, contentsOption, contentsFilter, categoryName)
            contentsViewModel.requestCategoryTaken()
        }
    }

    private fun initAdapter() {
        contentsAdapter = ContentsAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun setCategoryInfo() {
        categoryId = intent.getIntExtra("categoryId", 0).also { binding.categoryId = it }
        intent.getStringExtra("categoryName")?.let {
            categoryName = it
        }
        setCategoryName()
    }

    private fun initValue() {
        // 레이아웃 초기화
        layout = LINEAR_MIN_LAYOUT
        // 옵션 및 필터 초기화, 카테고리아이디가 -2이면 확인한 콘텐츠만 확인
        contentsOption = if (categoryId == -2) "true" else "all"
        contentsFilter = "created_at"
    }

    private fun setCategoryName() {
        contentsViewModel.setCategoryName(categoryName)
    }

    // 삭제된 카테고리라면 종료하는 함수
    private fun deletedCategory() {
        if (isDelete) {
            isDelete = false
            finish()
            CustomToast.showTextToast(this, "카테고리가 삭제되었습니다")
        }
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            loadState.observe(this@ContentsActivity) {
                // 서버 불러오는 중이라면 스켈레톤 화면 및 shimmer 효과를 보여줌
                with(binding) {
                    if (it) {
                        sflContents.startShimmer()
                        sfLCount.startShimmer()
                    } else {
                        sflContents.stopShimmer()
                        sfLCount.stopShimmer()
                    }
                }
            }

            contentsCount.observe(this@ContentsActivity) {
                // 콘텐츠 개수에 따른 visibility 조정
                with(binding) {
                    if (it == 0) {
                        rvContents.visibility = GONE
                        clEmpty.visibility = VISIBLE
                    } else {
                        rvContents.visibility = VISIBLE
                        clEmpty.visibility = GONE
                    }
                }
            }
            contentsList.observe(this@ContentsActivity) {
                // 콘텐츠 데이터 업데이트
                contentsAdapter.submitList(it.toList())
            }

            // 카테고리 제목
            contentsCategoryList.observe(this@ContentsActivity) {
                for (item in contentsCategoryList.value!!)
                    categoryList.add(item)
            }
        }
    }

    private fun changeLayout() {
        binding.ivLayout.setOnClickListener {
            // 기존 viewholder를 binding하는 것을 막기 위해 제거
            binding.rvContents.removeAllViews()

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
            SaveFragment(categoryName).show(supportFragmentManager, "언니 사랑해")
        }
    }

    // 최신순, 과거순, 최근 조회순 다이얼로그별 화면 설정
    private fun setOrderDialog() {
        binding.clOrder.setOnClickListener {
            val dialog = DialogContentsFilterFragment()
            dialog.show(supportFragmentManager, "contentsOrder")

            // 순서 클릭 시 이벤트 정의
            dialog.setFilterClickListener(object :
                DialogContentsFilterFragment.OnFilterClickListener {
                override fun onClick(filter: String) {
                    contentsFilter = filter
                    binding.tvOrder.text = when (filter) {
                        "created_at" -> "최신순"
                        "reverse" -> "과거순"
                        else -> "최근 조회순"
                    }
                    // 서버 호출
                    setContentsData()
                    dialog.dismiss()
                }
            })
        }
    }

    private fun setCategoryListDialog() {
        binding.clCategory.setOnClickListener {
            DialogContentsCategoryFragment(categoryList, categoryName).show(
                supportFragmentManager,
                "categoryList"
            )
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
    private fun clickItemMore() {
        contentsAdapter.setItemSetClickListner(object : ContentsAdapter.OnItemSetClickListener {
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
                // 더보기 -> 삭제 클릭 시 수행될 삭제 함수
                val removeItem: (Int) -> Unit = {
                    contentsAdapter.notifyItemRemoved(it)
                    contentsViewModel.decreaseContentsCount(1) // 콘텐츠 개수 1 감소
                }
                val dialog = ContentsMoreFragment(dataMore, removeItem, position)
                dialog.show(supportFragmentManager, "setting")
            }
        })
    }

    private fun setChipOrder() {
        with(binding) {
            chAll.setOnClickListener {
                contentsOption = "all"
                setContentsData()
            }
            chSeen.setOnClickListener {
                contentsOption = "true"
                setContentsData()
            }
            chUnseen.setOnClickListener {
                contentsOption = "false"
                setContentsData()
            }
            chAlarm.setOnClickListener {
                contentsOption = "notified"
                setContentsData()
            }
        }
    }

    private fun clickModify() {
        binding.tvModify.setOnClickListener {
            val intent = Intent(this, CategoryOrderModifyActivity::class.java)
            intent.putExtra("dataSet", true)
            startActivity(intent)
        }
    }

    // 해빗 클릭 시 이벤트 함수 정의
    private fun clickItemHavit() {
        contentsAdapter.setHavitClickListener(object : ContentsAdapter.OnItemHavitClickListener {
            override fun onHavitClick(v: ImageView, position: Int) {
                with(contentsAdapter) {
                    // 보지 않았던 콘텐츠의 경우 콘텐츠를 봤다는 토스트 띄우기
                    if (!currentList[position].isSeen) {
                        CustomToast.showDesignatedToast(
                            this@ContentsActivity,
                            R.layout.toast_havit_complete
                        )
                    }

                    currentList[position].isSeen = !currentList[position].isSeen
                    // 서버 호출
                    contentsViewModel.setIsSeen(currentList[position].id)
                    // 태그 바꾸기
                    val isSeen = (v.tag == "seen")
                    v.tag = if (isSeen) "unseen" else "seen"
                    v.setImageResource(if (isSeen) R.drawable.ic_contents_unread else R.drawable.ic_contents_read_2)

                    // 본 콘텐츠 목록에서 해빗 해제 시 제거
                    if ((contentsOption == "true" || contentsFilter == "seen_at") && v.tag == "unseen") {
                        contentsAdapter.notifyItemRemoved(position)
                    }
                    // 안 본 콘텐츠 목록에서 해빗 등록 시 제거
                    else if (contentsOption == "false" && v.tag == "seen") {
                        contentsAdapter.notifyItemRemoved(position)
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

        var categoryId = 0
        var categoryName = "error"
        var contentsOption = "all" // chip의 옵션 (전체/안봤어요/봤어요/알람)
        var contentsFilter = "created_at" // 정렬 필터 (최신순/과거순/최근조회순)

        var isDelete = false // 삭제된 카테고리인지 판별하는 변수
    }
}