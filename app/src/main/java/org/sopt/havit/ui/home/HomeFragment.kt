package org.sopt.havit.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents_simple.ContentsSimpleActivity
import org.sopt.havit.ui.notification.NotificationActivity
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by lazy { HomeViewModel(requireContext()) }
    private val contentsAdapter: HomeRecentContentsRvAdapter by lazy { HomeRecentContentsRvAdapter() }
    private lateinit var recommendRvAdapter: HomeRecommendRvAdapter
    private lateinit var categoryVpAdapter: HomeCategoryVpAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vmHome = homeViewModel
        binding.layoutCategory.vmHome = homeViewModel
        binding.layoutCategoryEmpty.vmHome = homeViewModel

        // 스크롤 시 검색뷰 상단에 고정시킴
        initSearchSticky()
        // adapter 초기화
        initAdpater()
        // 추천 콘텐츠
        recommendationDataObserve()
        setClickEvent() // Every clickEvent

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setData()
        categoryDataObserve()       // 카테고리 초기화
        recentContentsDataObserve() // 추천콘텐츠 초기화
        initProgressBar()   // 도달률 data 초기화
    }

    // 추천 콘텐츠 클릭 -> 웹뷰로 이동
    private fun clickRecommendItemView() {
        recommendRvAdapter.setItemClickListener(object :
            HomeRecommendRvAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                val intent = Intent(v.context, WebActivity::class.java)
                homeViewModel.recommendList.value?.get(position)
                    ?.let {
                        intent.putExtra("url", it.url)
                        intent.putExtra("contentsId", -1)
                    }
                startActivity(intent)
            }
        })
    }

    // 최근 저장 콘텐츠 클릭 -> 웹뷰로 이동
    private fun clickRecentContentsItemView() {
        contentsAdapter.setItemClickListener(object :
            HomeRecentContentsRvAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                val intent = Intent(v.context, WebActivity::class.java)
                homeViewModel.contentsList.value?.get(position)
                    ?.let {
                        intent.putExtra("url", it.url)
                        intent.putExtra("contentsId", it.id)
                        intent.putExtra("isSeen", it.isSeen)
                    }
                startActivity(intent)
            }
        })
    }

    private fun setData() {
        homeViewModel.requestUserDataTaken()    // 도달률
        homeViewModel.requestContentsTaken()    // 최근 저장 콘텐츠
        homeViewModel.requestCategoryTaken()    // 카테고리
        homeViewModel.requestRecommendTaken()   // 추천 콘텐츠
    }

    private fun clickAddCategory() {
        binding.layoutCategoryEmpty.tvAddCategory.setOnClickListener {
            Log.d("HomeFragment", "home_add_category")
        }
    }

    private fun initAdpater() {
        // 카테고리 adapter 초기화
        categoryVpAdapter = HomeCategoryVpAdapter()
        binding.layoutCategory.vpCategory.adapter = categoryVpAdapter
        // 카테고리 indicator 초기화
        val indicator = binding.layoutCategory.indicatorCategory
        indicator.setViewPager2(binding.layoutCategory.vpCategory)
        // 최근저장 콘텐츠 adapter 초기화
        binding.rvContents.adapter = contentsAdapter
    }

    private fun categoryDataObserve() {
        with(homeViewModel) {
            userData.observe(viewLifecycleOwner) { userData ->
                categoryData.observe(viewLifecycleOwner) { data ->
                    with(binding) {
                        if (data.isEmpty()) {
                            layoutCategory.clHomeCategory.visibility = View.GONE
                            clickAddCategory()
                        } else {
                            layoutCategoryEmpty.clHomeCategoryEmpty.visibility = View.GONE
                            categoryVpAdapter.categoryList.clear()
                            val list = setList(data, userData.totalContentNumber)
                            categoryVpAdapter.categoryList.addAll(list)
                            categoryVpAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    // popUp 삭제 버튼 클릭 시 수행되는 animation + popUp.visibility.GONE
    private fun deletePopup() {
        binding.clPopup.animate()
            .translationY(binding.clPopup.height.toFloat() * -1)
            .alpha(0.0f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    binding.clPopup.visibility = View.GONE
                }
            })
    }

    private fun setClickEvent() {
        binding.ivDeletePopup.setOnClickListener {
            deletePopup()
        }
        binding.ivAlarm.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }
        binding.clReachContents.setOnClickListener {
            val intent = Intent(requireActivity(), ContentsSimpleActivity::class.java)
            intent.putExtra("before", "unseen")
            startActivity(intent)
        }
        binding.layoutCategory.tvCategoryAll.setOnClickListener {
            val intent = Intent(requireActivity(), HomeCategoryAllActivity::class.java)
            startActivity((intent))
        }
        binding.clSearchClickable.setOnClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
        }
        binding.tvMoreContents.setOnClickListener {
            val intent = Intent(requireActivity(), ContentsSimpleActivity::class.java)
            intent.putExtra("before", "recent")
            startActivity(intent)
        }
    }

    private fun recommendationDataObserve() {
        with(homeViewModel) {
            recommendList.observe(viewLifecycleOwner) { data ->
                with(binding) {
                    if (data.isNotEmpty()) {
                        recommendRvAdapter = HomeRecommendRvAdapter()
                        rvRecommend.adapter = recommendRvAdapter
                        clickRecommendItemView()
                        recommendRvAdapter.recommendList.addAll(data)
                        recommendRvAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun initSearchSticky() {
        binding.svMain.run {
            header = binding.clStickyView
        }
    }

    private fun recentContentsDataObserve() {
        with(homeViewModel) {
            contentsList.observe(viewLifecycleOwner) { data ->
                with(binding) {
                    if (data.isEmpty()) {
                        rvContents.visibility = View.GONE
                        tvMoreContents.visibility = View.INVISIBLE
                    } else {
                        clContentsEmpty.visibility = View.GONE
                        val min = if (data.size < 10) data.size else 10
                        val list = data.subList(0, min)
                        clickRecentContentsItemView()
                        contentsAdapter.updateList(list)
                    }
                }
            }
        }
    }

    private fun initProgressBar() {
        with(homeViewModel) {
            userData.observe(viewLifecycleOwner) {
                // 전체 콘텐츠 수 or 본 콘텐츠 수가 0일 경우 예외처리
                var rate = 0
                if (it.totalSeenContentNumber != 0 && it.totalContentNumber != 0) { // 콘텐츠 수가 0이 아니라면 rate 계산
                    rate =
                        (it.totalSeenContentNumber.toDouble() / it.totalContentNumber.toDouble() * 100).toInt()
                }
                requestReachRate(rate)
            }
        }
    }
}