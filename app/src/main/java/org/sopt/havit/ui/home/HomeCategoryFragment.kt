package org.sopt.havit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeCategoryBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class HomeCategoryFragment :
    BaseBindingFragment<FragmentHomeCategoryBinding>(R.layout.fragment_home_category) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var categoryVpAdapter: HomeCategoryVpAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vmHome = homeViewModel

        initVpAdapter()
        initIndicator()

        return binding.root
    }

    private fun initIndicator() {
        val indicator = binding.indicatorCategory
        indicator.setViewPager2(binding.vpCategory)
    }

    private fun initVpAdapter() {
        categoryVpAdapter = HomeCategoryVpAdapter()
        binding.vpCategory.adapter = categoryVpAdapter

        homeViewModel.categoryData.observe(viewLifecycleOwner) {
            categoryVpAdapter.setList(it)
        }
    }

}