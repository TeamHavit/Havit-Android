package org.sopt.havit.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.data.HomeContentsData
import org.sopt.havit.data.HomeRecommendData
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var contentsAdapter: HomeRecentContentsRvAdapter
    private lateinit var recommendRvAdapter: HomeRecommendRvAdapter
    private lateinit var action: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vmHome = homeViewModel

        initSearchSticky()
        initCategoryFragment()
        initProgressBar()
        initContentsView()
        initContentsRvAdapter()
        initRecommendRvAdapter()

        setClickEvent()

        return binding.root
    }

    private fun setClickEvent() {
        binding.ivAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_homeAlarmFragment)
        }
        binding.tvReachContents.setOnClickListener {
            action = HomeFragmentDirections.actionNavigationHomeToHomeContentsFragment("unseen")
            findNavController().navigate(action)
        }
        binding.clSearch.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_searchFragment)
        }
        binding.tvCategoryAll.setOnClickListener {
            val intent = Intent(requireActivity(), HomeCategoryAllActivity::class.java)
            startActivity(intent)
        }
        binding.tvMoreContents.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_homeContentsFragment)
        }
    }

    private fun initContentsView() {
        binding.clContentsEmpty.visibility = View.GONE

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
            HomeContentsData("", "카테고리 이름1", "헤더입니다 헤더입니다 헤더입니다 헤더임", "2021.11.24"),
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

    private fun initCategoryFragment() {
        val fragmentHomeCategory = HomeCategoryFragment()
        val fragmentHomeCategoryEmpty = HomeCategoryEmptyFragment()

        childFragmentManager.beginTransaction()
            .add(R.id.fcv_category, fragmentHomeCategory)
            .commit()
//        childFragmentManager.beginTransaction()
//            .add(R.id.fcv_category, fragmentHomeCategoryEmpty)
//            .commit()
    }

}