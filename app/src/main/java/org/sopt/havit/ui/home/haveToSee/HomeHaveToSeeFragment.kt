package org.sopt.havit.ui.home.haveToSee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeHaveToSeeBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.home.HomeViewModel

class HomeHaveToSeeFragment :
    BaseBindingFragment<FragmentHomeHaveToSeeBinding>(R.layout.fragment_home_have_to_see) {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }
}