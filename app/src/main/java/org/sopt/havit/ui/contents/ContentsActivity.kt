package org.sopt.havit.ui.contents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.ActivityContentsBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.category.CategoryContentModifyActivity
import org.sopt.havit.ui.category.CategoryViewModel
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.CustomToast

class ContentsActivity : BaseBindingActivity<ActivityContentsBinding>(R.layout.activity_contents) {
    private lateinit var contentsAdapter: ContentsAdapter
    private val contentsViewModel: ContentsViewModel by lazy { ContentsViewModel(this) }
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(this) }
    private var contentsCategoryList = ArrayList<CategoryResponse.AllCategoryData>()
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.contentsViewModel = contentsViewModel
        binding.vmCategory = categoryViewModel

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
        getModifyData()
        clickItemHavit()
        clickItemMore()
    }

    override fun onStart() {
        super.onStart()
        setContentsData()
    }

    private fun setContentsData() {
        if (categoryId <= -1) {
            contentsViewModel.requestContentsAllTaken(contentsOption, contentsFilter, categoryName)
        } else {
            contentsViewModel.requestContentsTaken(categoryId, contentsOption, contentsFilter)
        }
    }

    private fun initAdapter() {
        contentsAdapter = ContentsAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun setCategoryInfo() {
        // 카테고리 전체 정보를 서버를 통해 호출
        categoryViewModel.requestCategoryTaken()

        // 카테고리 뷰에서 넘겨받은 데이터를 ContentsActivity의 변수에 할당
        categoryId = intent.getIntExtra("categoryId", 0).also { binding.categoryId = it }
        intent.getStringExtra("categoryName")?.let {
            categoryName = it
        }
        imageId = intent.getIntExtra("imageId", 0)
        categoryPosition = intent.getIntExtra("position", 0)
        // 카테고리 이름 재설정
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
        // categoryName 으로 뷰모델의 카테고리 이름 변수 변경
        contentsViewModel.setCategoryName(categoryName)
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
            contentsList.observe(this@ContentsActivity) {
                // 콘텐츠 데이터 업데이트
                contentsAdapter.submitList(it.toList())
            }
        }
        // 카테고리 리스트를 가져옴 (ArrayList에 넣어 mutable하게 만듦)
        categoryViewModel.categoryList.observe(this@ContentsActivity) {
            contentsCategoryList =
                (categoryViewModel.categoryList.value as ArrayList<CategoryResponse.AllCategoryData>?)!!
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
            DialogContentsCategoryFragment(contentsCategoryList, categoryName).show(
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
                    val list =
                        contentsAdapter.currentList.toMutableList() // mutable로 해주어야 삭제(수정) 가능
                    list.removeAt(it)
                    // 뷰모델의 콘텐츠 리스트 변수를 업데이트 -> observer를 통해 adapter의 list도 업데이트 된다
                    contentsViewModel.updateContentsList(list)
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

    // 수정버튼을 클릭했을 때
    private fun clickModify() {
        binding.tvModify.setOnClickListener {
            // 카테고리 이름 list
            val categoryTitleList = ArrayList<String>()
            for (item in categoryViewModel.categoryList.value!!)
                categoryTitleList.add(item.title)

            // 카테고리 수정 뷰로 넘길 intent
            val intent = Intent(this, CategoryContentModifyActivity::class.java).apply {
                putExtra("categoryId", categoryId)
                putExtra("categoryName", categoryName)
                putExtra("imageId", imageId)
                putStringArrayListExtra("categoryNameList", categoryTitleList)
            }

            getResult.launch(intent)
        }
    }

    // 수정 뷰에서 온 수정/삭제 정보를 가져옴
    private fun getModifyData() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> { // 삭제
                    // 서버에 삭제 요청
                    categoryViewModel.requestCategoryDelete(categoryId)
                    finish()
                }
                RESULT_FIRST_USER -> { // 카테고리 이름 & 아이콘 수정
                    // 수정할 카테고리의 정보를 받아옴
                    categoryName = it.data?.getStringExtra("categoryName") ?: "null"
                    imageId = it.data?.getIntExtra("imageId", 0) ?: 0
                    contentsCategoryList[categoryPosition].apply {
                        title = categoryName
                        imageId = Companion.imageId
                    }
                    // 서버에 수정된 내용 전달
                    categoryViewModel.requestCategoryContent(categoryId, imageId, categoryName)
                    // 카테고리 이름 수정
                    setCategoryName() // 뷰모델의 카테고리 이름 변수 재설정
                    binding.tvCategory.text = categoryName
                }
            }
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
        var imageId = 0
        var categoryPosition = 0
        var contentsOption = "all" // chip의 옵션 (전체/안봤어요/봤어요/알람)
        var contentsFilter = "created_at" // 정렬 필터 (최신순/과거순/최근조회순)
    }
}