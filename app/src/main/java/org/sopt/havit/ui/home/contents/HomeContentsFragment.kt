package org.sopt.havit.ui.home.contents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeCategoryBinding
import org.sopt.havit.databinding.FragmentHomeContentsBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.home.HomeViewModel

class HomeContentsFragment :
    BaseBindingFragment<FragmentHomeContentsBinding>(R.layout.fragment_home_contents) {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}