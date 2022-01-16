package org.sopt.havit.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.sopt.havit.R
import org.sopt.havit.data.HomeContentsData
import org.sopt.havit.data.HomeRecommendData
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents_simple.ContentsSimpleActivity
import org.sopt.havit.ui.notification.NotificationActivity

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var contentsAdapter: HomeRecentContentsRvAdapter
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

        initSearchSticky()
        initProgressBar()
        initContentsView()
        initCategoryView()
        initVpAdapter()
        initIndicator()
        initContentsRvAdapter()
        initRecommendRvAdapter()

        setClickEvent()

        return binding.root
    }

    private fun initCategoryView() {
        // Category가 존재할 경우
        initVpAdapter()
        initIndicator()
        homeViewModel.requestUserName("안나영")
        binding.layoutCategoryEmpty.clHomeCategoryEmpty.visibility = View.GONE

        // Category가 존재하지 않을 경우
//        binding.layoutCategory.clHomeCategory.visibility = View.GONE
//        clickAddCategory()

    }

    private fun clickAddCategory() {
        binding.layoutCategoryEmpty.tvAddCategory.setOnClickListener {
            Log.d("HomeFragment", "home_add_category")
        }
    }

    private fun initIndicator() {
        val indicator = binding.layoutCategory.indicatorCategory
        indicator.setViewPager2(binding.layoutCategory.vpCategory)
    }

    private fun initVpAdapter() {
        categoryVpAdapter = HomeCategoryVpAdapter()
        binding.layoutCategory.vpCategory.adapter = categoryVpAdapter

        homeViewModel.categoryData.observe(viewLifecycleOwner) {
            categoryVpAdapter.setList(it)
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
        binding.tvReachContents.setOnClickListener {
            action = HomeFragmentDirections.actionNavigationHomeToHomeContentsFragment("unseen")
            findNavController().navigate(action)
        }
        binding.clSearch.setOnClickListener {
            //findNavController().navigate(R.id.action_navigation_home_to_searchFragment)
            val intent = Intent(requireActivity(), ContentsSimpleActivity::class.java)
            intent.putExtra("before", "unseen")
            startActivity(intent)
        }
        binding.layoutCategory.tvCategoryAll.setOnClickListener {
            Log.d("activity_check", "CLICK TEST")
            val intent = Intent(requireActivity(), HomeCategoryAllActivity::class.java)
            startActivity((intent))
        }
        binding.clSearch.setOnClickListener {
            Log.d("homefragment_search", "SEARCH")
//            val intent = Intent(requireActivity(), SearchActivity::class.java)
//            startActivity(intent)
        }
        binding.tvMoreContents.setOnClickListener {
            val intent = Intent(requireActivity(), ContentsSimpleActivity::class.java)
            intent.putExtra("before", "recent_save")
            startActivity(intent)
        }
    }

    private fun initContentsView() {
        // 최근 저장 콘텐츠가 있을 경우
        binding.clContentsEmpty.visibility = View.GONE

        // 최근 저장 콘텐츠가 없을 경우
//        binding.rvContents.visibility = View.GONE
//        binding.tvMoreContents.visibility = View.INVISIBLE
    }

    private fun initRecommendRvAdapter() {
        recommendRvAdapter = HomeRecommendRvAdapter()
        binding.rvRecommend.adapter = recommendRvAdapter
        val list = listOf(
            HomeRecommendData("", "이름1", "종류 / 카테고리"),
            HomeRecommendData("", "이름2", "종류 / 카테고리"),
            HomeRecommendData("", "이름3", "종류 / 카테고리"),
            HomeRecommendData("", "이름4", "종류 / 카테고리"),
            HomeRecommendData("", "이름5", "종류 / 카테고리"),
            HomeRecommendData("", "이름6", "종류 / 카테고리"),
            HomeRecommendData("", "이름7", "종류 / 카테고리"),
            HomeRecommendData("", "이름8", "종류 / 카테고리"),
            HomeRecommendData("", "이름9", "종류 / 카테고리")
        )
        homeViewModel.requestRecommendTaken(list)
        homeViewModel.recommendList.observe(viewLifecycleOwner) {
            recommendRvAdapter.setList(it)
        }
        recommendRvAdapter.notifyDataSetChanged()
    }

    private fun initSearchSticky() {
        binding.svMain.run {
            header = binding.clStickyView
            stickListener = { _ ->
                Log.d("LOGGER_TAG", "stickListener")
            }
            freeListener = { _ ->
                Log.d("LOGGER_TAG", "freeListener")
            }
        }
    }

    private fun initContentsRvAdapter() {
        contentsAdapter = HomeRecentContentsRvAdapter()
        binding.rvContents.adapter = contentsAdapter
        val list = listOf(
            HomeContentsData("", "카테고리 이름11111111111", "헤더입니다 헤더입니다 헤더입니다 헤더입니다", "2021.11.24"),
            HomeContentsData("", "카테고리 이름2", "헤더입니다 헤더입니다 헤더입니다 헤더임", "2021.11.24"),
            HomeContentsData("", "카테고리 이름3", "헤더입니다", "2021.11.24"),
            HomeContentsData("", "카테고리 이름4", "헤더입니다 헤더입니다 헤더입니다 헤더임", "2021.11.24"),
            HomeContentsData("", "카테고리 이름5", "헤더입니다 헤더입니다 헤더입니다 헤더임", "2021.11.24"),
            HomeContentsData("", "카테고리 이름6", "헤더입니다 헤더입니다 헤더입니다 헤더임", "2021.11.24"),
            HomeContentsData("", "카테고리 이름7", "헤더입니다 헤더입니다 헤더입니다 헤더임", "2021.11.24"),
            HomeContentsData("", "카테고리 이름8", "헤더입니다 헤더입니다 헤더입니다 헤더임", "2021.11.24")
        )
        homeViewModel.requestContentsTaken(list)
        homeViewModel.contentsList.observe(viewLifecycleOwner) {
            contentsAdapter.setList(it)
        }
        contentsAdapter.notifyDataSetChanged()
    }

    private fun initProgressBar() {
        val read = 62.toDouble()
        val all = 145.toDouble()
        val rate: Int = ((read / all) * 100).toInt()
        Log.d("HomeFragment", "rate : $rate")
        binding.pbReach.progress = rate
    }
}