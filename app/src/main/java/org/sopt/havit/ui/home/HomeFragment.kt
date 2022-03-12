package org.sopt.havit.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents_simple.ContentsSimpleActivity
import org.sopt.havit.ui.notification.NotificationActivity
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.PopupSharedPreference

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by lazy { HomeViewModel(requireContext()) }
    private val contentsAdapter: HomeRecentContentsRvAdapter by lazy { HomeRecentContentsRvAdapter() }
    private lateinit var recommendRvAdapter: HomeRecommendRvAdapter
    private lateinit var categoryVpAdapter: HomeCategoryVpAdapter
    private var reachLevel = 0

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

        // 도달률 팝업 초기화
        initPopup()
        // 스크롤 시 검색뷰 상단에 고정시킴
        initSearchSticky()
        // adapter 초기화
        initAdpater()
        // 추천 콘텐츠
        recommendationDataObserve()
        // Every clickEvent
        setClickEvent()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setData()
        categoryDataObserve()       // 카테고리 초기화
        recentContentsDataObserve() // 추천콘텐츠 초기화
        initProgressBar()           // 도달률 data 초기화
    }

    // onCreateView에서 이루어지는 도달률 팝업 초기화
    private fun initPopup() {
        reachLevel = PopupSharedPreference.getReachLevel(requireContext())
        val isPopup = PopupSharedPreference.getIsPopup(requireContext())
        binding.clPopup.visibility = if (isPopup) View.VISIBLE else View.GONE
    }

    // onStart에서 이루어지는 userData 변화에 따른 도달률 팝업 업데이트
    private fun updatePopup() {
        checkReachLevel()   // 구간 변화 업데이트
        val isPopup = PopupSharedPreference.getIsPopup(requireContext())
        if (isPopup) {
            binding.clPopup.visibility = View.VISIBLE
        } else {
            checkDeletePopupTime() // (현재 시간 - x버튼 누른 시간) 계산
        }
    }

    // 도달률 구간변화 업데이트
    private fun checkReachLevel() {
        val prevLevel = PopupSharedPreference.getReachLevel(requireContext())
        // 도달률 구간에 변경이 있을 경우 PopupSharedPrefence 저장값 변경
        if (prevLevel != reachLevel) {
            with(PopupSharedPreference) {
                setReachLevel(requireContext(), reachLevel)
                setIsPopup(requireContext(), true)
            }
        }
    }

    private fun checkDeletePopupTime() {
        val currentTime = System.currentTimeMillis() / (1000 * 60 * 60) // 1970.01.01부터 현재까지 흐른 시간
        val deletePopupTime =
            PopupSharedPreference.getDeletePopupTime(requireContext())   // deletePopup 버튼을 누른 시각
        if ((currentTime - deletePopupTime) > 0) {  // 1시간이 지나면 도달률 팝업 VISIBLE :
            binding.clPopup.visibility = View.VISIBLE
            PopupSharedPreference.setIsPopup(requireContext(), true)
        } else {
            binding.clPopup.visibility = View.GONE
        }
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

    private fun clickDeletePopup() {
        // popUp 삭제 버튼 클릭 시 수행되는 animation
        val animation = TranslateAnimation(0.0f, 0.0f, 0.0f, binding.clPopup.height.toFloat() * -1)
        animation.duration = 300        // 300 millis 동안 수행
        animation.fillAfter = false     // fillAfter = false : 애니메이션 수행 후 view 원위치로
        binding.clPopup.startAnimation(animation)
        Handler(Looper.getMainLooper()).postDelayed({
            //딜레이 후 시작할 코드 작성 : animation 수행 후 팝업 vibility GONE으로 처리
            binding.clPopup.visibility = View.GONE
        }, 300)

        // MySharedPreference에 deletePopup버튼을 누른 현재 시각 저장
        val deletePopupTime = System.currentTimeMillis() / (1000 * 60) // 1970.01.01부터 흐른 시간
        PopupSharedPreference.setDeletePopupTime(requireContext(), deletePopupTime)
        PopupSharedPreference.setIsPopup(requireContext(), false)
    }

    private fun setClickEvent() {
        binding.ivDeletePopup.setOnClickListener {
            clickDeletePopup()
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

    private fun initLevel(rate: Int) = when {
        rate <= 33 -> 1
        rate in 34..66 -> 2
        rate in 67..99 -> 3
        else -> 4
    }

    private fun initProgressBar() {
        with(homeViewModel) {
            userData.observe(viewLifecycleOwner) {
                var rate = 0
                // 전체 콘텐츠 수 or 본 콘텐츠 수가 0일 경우 예외처리
                if (it.totalSeenContentNumber != 0 && it.totalContentNumber != 0) { // 콘텐츠 수가 0이 아니라면 rate 계산
                    rate =
                        (it.totalSeenContentNumber.toDouble() / it.totalContentNumber.toDouble() * 100).toInt()
                }
                requestReachRate(rate)
                reachLevel = initLevel(rate)    // 도달률 구간 설정
                updatePopup()                   // 도달률 팝업 업데이트
                Log.d("HOME_DELETE_POPUP", "reachLevel2: $reachLevel")
            }
        }
    }
}