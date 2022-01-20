package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.databinding.FragmentNoCategoryBinding
import org.sopt.havit.util.MySharedPreference

class NoCategoryFragment : Fragment() {

    private var _binding: FragmentNoCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoCategoryBinding.inflate(layoutInflater, container, false)

        getCategoryNum()
        initClickListener()

        return binding.root
    }

    private fun getCategoryNum() {
        lifecycleScope.launch {
            try {
                val response =
                    RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                        .getCategoryNum()
                val num = response.data.size
                initTransaction(num)
                Log.d("BottomSheetShareFragment", num.toString())
            } catch (e: Exception) {
            }
        }
    }

    private fun initTransaction(categoryNum: Int) {
        if (categoryNum != 0)
            findNavController().navigate(R.id.action_noCategoryFragment_to_selectCategoryFragment)
    }

    private fun initClickListener() {
        binding.btnAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_noCategoryFragment_to_addCategoryFragment)
        }
    }
}