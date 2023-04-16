package org.sopt.havit.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.category.CategoryAddActivity
import org.sopt.havit.ui.contents_simple.ContentsSimpleActivity
import org.sopt.havit.ui.notification.NotificationActivity
import org.sopt.havit.ui.search.SearchActivity
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.PopupSharedPreference
import org.sopt.havit.util.setDragSensitivity
import org.sopt.havit.util.setOnSingleClickListener
import org.sopt.havit.util.GoogleAnalyticsUtil
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_GO_BACK
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_REFRESH
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_SHARE
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_MUST_SEE_CONTENT
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_SEARCH_CONTENT
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_WHOLE_CATEGORY
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_HAVIT_SERVICE_GUIDE
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_SEE_MORE
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_RECOMMENDED_SITE

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val contentsAdapter: HomeRecentContentsRvAdapter by lazy { HomeRecentContentsRvAdapter() }
    private lateinit var recommendRvAdapter: HomeRecommendRvAdapter
    private lateinit var categoryVpAdapter: HomeCategoryVpAdapter
    private var isPopup = false
    private var popupText = ""

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
        initAdapter()
        // 추천 콘텐츠
        recommendationDataObserve()
        // Every clickEvent
        setClickEvent()
        disableRvSmoothScroll()
        setVpSensitivity()

        categoryDataObserve()   // 카테고리 초기화
        recentContentsDataObserve() // 최근저장 콘텐츠 초기화
        notificationDataObserve()   // 알림 아이콘 초기화
        initReachRate() // 도달률 관련 데이터 초기화
        addSourceOnIsReadyToSetCategory()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onDestroyView() {
        homeViewModel.removeSourceOnIsReadyToSetCategory()
        super.onDestroyView()
    }

    private fun addSourceOnIsReadyToSetCategory() {
        homeViewModel.addSourceOnIsReadyToSetCategory()
    }

    private fun disableRvSmoothScroll() {
        binding.svMain.isSmoothScrollingEnabled = false
    }

    private fun setVpSensitivity() {
        binding.layoutCategory.vpCategory.setDragSensitivity(0.1f)
    }

    // 추천 콘텐츠 클릭 -> 웹뷰로 이동
    private fun clickRecommendItemView() {
        recommendRvAdapter.setItemClickListener(object :
            HomeRecommendRvAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                GoogleAnalyticsUtil.logClickEventWithRecommendedSiteNum(CLICK_RECOMMENDED_SITE, position)

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
        with(homeViewModel) {
            requestUserDataTaken() // 도달률
            requestContentsTaken() // 최근 저장 콘텐츠
            requestCategoryTaken() // 카테고리
            requestRecommendTaken() // 추천 콘텐츠
            requestNotificationTaken()  // 알림 예정 콘텐츠
            loadStateObserve()
        }
    }

    private fun loadStateObserve() {
        with(homeViewModel) {
            loadState.observe(viewLifecycleOwner) { state ->
                if (state == NetworkState.LOADING) // 로딩중이라면
                    binding.sflHome.startShimmer()
                else // 로딩이 끝났다면
                    binding.sflHome.stopShimmer()
            }

            // 유저, 카테고리, 최근저장콘텐츠, 추천콘텐츠 서버 연결 확인
            userLoadState.observe(viewLifecycleOwner) { checkLoadState() }
            categoryLoadState.observe(viewLifecycleOwner) { checkLoadState() }
            recommendLoadState.observe(viewLifecycleOwner) { checkLoadState() }
            contentsLoadState.observe(viewLifecycleOwner) { checkLoadState() }
            notificationLoadState.observe(viewLifecycleOwner) { checkLoadState() }
        }
    }

    private fun initAdapter() {
        // 카테고리 adapter 초기화
        categoryVpAdapter = HomeCategoryVpAdapter()
        binding.layoutCategory.vpCategory.adapter = categoryVpAdapter
        // 카테고리 indicator 초기화
        val indicator = binding.layoutCategory.indicatorCategory
        indicator.attachTo(binding.layoutCategory.vpCategory)
        // 최근저장 콘텐츠 adapter 초기화
        binding.rvContents.adapter = contentsAdapter
        // 추천 콘텐츠 adapter 초기화
        recommendRvAdapter = HomeRecommendRvAdapter()
        binding.rvRecommend.adapter = recommendRvAdapter
    }

    // 최근 저장 콘텐츠
    private fun recentContentsDataObserve() {
        with(homeViewModel) {
            contentsList.observe(viewLifecycleOwner) { data ->
                if (data.isNotEmpty()) {
                    val min = if (data.size < 10) data.size else 10
                    val list = data.subList(0, min)
                    contentsAdapter.updateList(list)
                }
            }
        }
    }

    private fun categoryDataObserve() {
        with(homeViewModel) {
            isReadyToSetCategory.observe(viewLifecycleOwner) { isServerLoadComplete ->
                if (isServerLoadComplete) {
                    val list = setList()
                    categoryVpAdapter.updateList(list)
                    binding.layoutCategory.indicatorCategory.attachTo(binding.layoutCategory.vpCategory)
                }
            }
        }
    }

    private fun notificationDataObserve() {
        with(homeViewModel) {
            notificationList.observe(viewLifecycleOwner) { data ->
                binding.hasNotification = data.isNotEmpty()
            }
        }
    }

    // popUp 삭제 버튼 클릭 이벤트
    private fun clickDeletePopup() {
        // popUp 삭제 버튼 클릭 시 수행되는 animation
        val animation =
            TranslateAnimation(0.0f, 0.0f, 0.0f, binding.clPopup.height.toFloat() * -1)
        animation.duration = 300 // 300 millis 동안 수행
        animation.fillAfter = false // fillAfter = false : 애니메이션 수행 후 view 원위치로
        binding.clPopup.startAnimation(animation)
        Handler(Looper.getMainLooper()).postDelayed({
            // 딜레이 후 시작할 코드 작성 : animation 수행 후 팝업 vibility GONE으로 처리
            binding.isPopup = false
        }, 300)

        // MySharedPreference에 deletePopup버튼을 누른 현재 시각 저장
        val deletePopupTime =
            System.currentTimeMillis() / (1000 * 60) // 1970.01.01부터 흐른 시간(분)
        PopupSharedPreference.setDeletePopupTime(requireContext(), deletePopupTime)
        PopupSharedPreference.setIsPopup(requireContext(), false)
    }

    private fun setClickEvent() {
        binding.layoutCategoryEmpty.tvAddCategory.setOnClickListener {
            val intent = Intent(requireActivity(), CategoryAddActivity::class.java)
            startActivity(intent)
        }
        binding.ivDeletePopup.setOnClickListener {
            clickDeletePopup()
        }
        binding.ivAlarm.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }
        binding.clReachContents.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_MUST_SEE_CONTENT)
            val intent = Intent(requireActivity(), ContentsSimpleActivity::class.java)
            intent.putExtra(CONTENT_TYPE, "unseen")
            startActivity(intent)
        }
        binding.layoutCategory.tvCategoryAll.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_WHOLE_CATEGORY)
            val intent = Intent(requireActivity(), HomeCategoryAllActivity::class.java)
            startActivity((intent))
        }
        binding.clSearchClickable.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_SEARCH_CONTENT)
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
        }
        binding.tvMoreContents.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_SEE_MORE)
            val intent = Intent(requireActivity(), ContentsSimpleActivity::class.java)
            intent.putExtra(CONTENT_TYPE, "recent")
            startActivity(intent)
        }
        binding.ivServiceGuide.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_HAVIT_SERVICE_GUIDE)
            val intent = Intent(requireActivity(), ServiceGuideActivity::class.java)
            startActivity(intent)
        }
        binding.layoutNetworkError.ivRefresh.setOnSingleClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    requireActivity(),
                    R.anim.rotation_refresh
                )
            )
            setData()
        }
        clickRecommendItemView() // 추천콘텐츠 클릭->웹뷰로 이동
        clickRecentContentsItemView() // 최근저장 콘텐츠 클릭->웹뷰로 이동
    }

    private fun recommendationDataObserve() {
        with(homeViewModel) {
            recommendList.observe(viewLifecycleOwner) { data ->
                if (data.isNotEmpty()) {
                    recommendRvAdapter.recommendList.clear()
                    recommendRvAdapter.recommendList.addAll(data)
                    recommendRvAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initSearchSticky() {
        binding.svMain.run {
            header = binding.clStickyView
        }
    }

    // 도달률 관련 데이터(팝업, 도달률 그래프 등) 초기화
    private fun initReachRate() {
        with(homeViewModel) {
            userData.observe(viewLifecycleOwner) {
                val rate = setReachRate(it) // 도달률 계산
                initPopup(rate)
            }
        }
    }

    // 계산한 도달률(rate)로 popupText string값 지정
    private fun setPopupText(rate: Int): String =
        when (rate) {
            -1 -> getString(R.string.home_popup_description0)
            in 0..33 -> getString(R.string.home_popup_description1)
            in 34..66 -> getString(R.string.home_popup_description2)
            in 67..99 -> getString(R.string.home_popup_description3)
            100 -> getString(R.string.home_popup_description4)
            else -> getString(R.string.home_popup_description1)
        }

    // 도달률 팝업 초기화
    private fun initPopup(rate: Int) {
        isPopup = PopupSharedPreference.getIsPopup(requireContext())
        popupText = setPopupText(rate) // 측정한 도달률로 popupText string값 지정
        checkPopupText() // 도달률 구간변화 검사
        // isPopup 값이 false일 경우 팝업 시간 변화 검사
        if (!isPopup)
            checkDeletePopupTime()
        updatePopup() // 도달률 최종 업데이트
    }

    // 도달률 구간변화 검사
    private fun checkPopupText() {
        val prevPopupText = PopupSharedPreference.getPopupText(requireContext())
        // 도달률 구간에 변경이 있을 경우 isPopup 값 true로 변경
        if (prevPopupText != popupText) {
            isPopup = true
        }
    }

    // 팝업 시간 변화 검사 : x를 누르고 3일이 지났는지
    private fun checkDeletePopupTime() {
        val currentTime =
            System.currentTimeMillis() / (1000 * 60) // 1970.01.01부터 현재까지 흐른 시간(분)
        val deletePopupTime =
            PopupSharedPreference.getDeletePopupTime(requireContext()) // deletePopup 버튼을 누른 시각
        isPopup = (currentTime - deletePopupTime) > 60 * 24 * 3
    }

    // 도달률 최종 업데이트
    private fun updatePopup() {
        binding.isPopup = isPopup // 최종 isPopup 값 뷰에 반영
        PopupSharedPreference.setIsPopup(
            requireContext(),
            isPopup
        ) // 최종 isPopup 값 spf에 저장
        PopupSharedPreference.setPopupText(
            requireContext(),
            popupText
        ) // 도달률 텍스트 값 spf에 저장
    }

    companion object {
        const val CONTENT_TYPE = "contentType"
    }
}
