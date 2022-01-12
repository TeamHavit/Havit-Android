package org.sopt.havit.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment


class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vmHome = homeViewModel

        createFragment()
        return binding.root
    }

    private fun createFragment() {
        val fragmentHomeSearch = HomeSearchFragment()
        val fragmentHomeCategory = HomeCategoryFragment()

        childFragmentManager.beginTransaction()
            .add(R.id.fcv_search, fragmentHomeSearch)
            .add(R.id.fcv_category, fragmentHomeCategory)
            .commit()
    }

}