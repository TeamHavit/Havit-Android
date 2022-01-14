package org.sopt.havit.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.sopt.havit.databinding.FragmentCategoryContentModifyBinding

class CategoryContentModifyFragment : Fragment() {
    private var _binding: FragmentCategoryContentModifyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryContentModifyBinding.inflate(inflater, container, false)

        clickBack()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickBack(){
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}