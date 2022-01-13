package org.sopt.havit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.sopt.havit.R
import org.sopt.havit.data.HomeContentsData
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var contentsAdapter: HomeContentsRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vmHome = homeViewModel

        initSearchSticky()
        initFragment()
        initProgressBar()
//        initContentsLayout()
        initContentsRvAdapter()

        return binding.root
    }

    private fun initSearchSticky() {
        binding.svMain.run {
            header = binding.fcvSearch
            stickListener = { _ ->
                Log.d("LOGGER_TAG", "stickListener")
            }
            freeListener = { _ ->
                Log.d("LOGGER_TAG", "freeListener")
            }
        }
    }


    private fun initContentsRvAdapter() {
        contentsAdapter = HomeContentsRvAdapter()
        binding.layoutHomeContents.rvContents.adapter = contentsAdapter
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

    private fun initContentsLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_home_contents_empty, null)
        val layout = binding.clHomeContents
        layout.addView(view)
    }

    private fun initProgressBar() {
        val read = 62.toDouble()
        val all = 145.toDouble()
        val rate: Int = ((read / all) * 100).toInt()
        Log.d("HomeFragment", "rate : $rate")
        binding.pbReach.progress = rate
    }

    private fun initFragment() {
        val fragmentHomeSearch = HomeSearchFragment()
        val fragmentHomeCategory = HomeCategoryFragment()
//        val fragmentHomeCategoryEmpty = HomeCategoryEmptyFragment()

        childFragmentManager.beginTransaction()
            .add(R.id.fcv_search, fragmentHomeSearch)
            .commit()

        childFragmentManager.beginTransaction()
            .add(R.id.fcv_category, fragmentHomeCategory)
            .commit()
    }

}