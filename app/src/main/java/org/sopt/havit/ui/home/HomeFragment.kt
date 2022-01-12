package org.sopt.havit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

        initFragment()
        return binding.root
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