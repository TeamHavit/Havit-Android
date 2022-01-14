package org.sopt.havit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentHomeCategoryEmptyBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class HomeCategoryEmptyFragment :
    BaseBindingFragment<FragmentHomeCategoryEmptyBinding>(R.layout.fragment_home_category_empty) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        clickBtnAddCategory()
        return binding.root
    }

    private fun clickBtnAddCategory() {
        binding.tvAddCategory.setOnClickListener {
            val intent = Intent(requireActivity(), HomeAddCategoryActivity::class.java)
            startActivity(intent)
        }
    }


}