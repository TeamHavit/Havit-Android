package org.sopt.havit.ui.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentNoCategoryBinding

class NoCategoryFragment : Fragment() {

    private var _binding: FragmentNoCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoCategoryBinding.inflate(layoutInflater, container, false)

        initClickListener()

        return binding.root
    }

    private fun initClickListener(){
        binding.btnAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_noCategoryFragment_to_addCategoryFragment)
        }
    }
}