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
import org.sopt.havit.data.HomeRecommendData
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents_simple.ContentsSimpleActivity
import org.sopt.havit.ui.notification.NotificationActivity
import org.sopt.havit.ui.search.SearchActivity

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

        setData()
        initSearchSticky()
        initProgressBar()
        initCategoryView()
        initVpAdapter()
        initIndicator()
        initContentsRvAdapter()
        initRecommendRvAdapter()

        setClickEvent()

        return binding.root
    }

    private fun setData() {
        homeViewModel.requestContentsTaken()
    }

    private fun initCategoryView() {
        // Category가 존재할 경우
        initVpAdapter()
        initIndicator()
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

    private fun initVpAdapter() {
        categoryVpAdapter = HomeCategoryVpAdapter()
        binding.layoutCategory.vpCategory.adapter = categoryVpAdapter

        homeViewModel.categoryData.observe(viewLifecycleOwner) {
            categoryVpAdapter.setList(it)
        }
    }

    private fun initIndicator() {
        val indicator = binding.layoutCategory.indicatorCategory
        indicator.setViewPager2(binding.layoutCategory.vpCategory)
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
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
        }
        binding.tvMoreContents.setOnClickListener {
            val intent = Intent(requireActivity(), ContentsSimpleActivity::class.java)
            intent.putExtra("before", "recent")
            startActivity(intent)
        }
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
        recentContentsDataObserve()
    }

    private fun recentContentsDataObserve() {
        with(homeViewModel) {
            contentsList.observe(viewLifecycleOwner) { data ->
                with(binding) {
                    if (data.isEmpty()) {
                        binding.rvContents.visibility = View.GONE
                        binding.tvMoreContents.visibility = View.INVISIBLE
                    } else {
                        binding.clContentsEmpty.visibility = View.GONE
                        val min = if (data.size < 10) data.size else 10
                        val list = data.subList(0, min)
                        contentsAdapter.contentsList.addAll(list)
                        contentsAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun initProgressBar() {
        homeViewModel.reachRate.observe(viewLifecycleOwner) {
            binding.pbReach.progress = it
        }
    }
}