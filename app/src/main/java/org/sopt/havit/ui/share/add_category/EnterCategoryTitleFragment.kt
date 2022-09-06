package org.sopt.havit.ui.share.add_category

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.databinding.FragmentEnterCategoryTitleBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard
import org.sopt.havit.util.MySharedPreference

class EnterCategoryTitleFragment :
    BaseBindingFragment<FragmentEnterCategoryTitleBinding>(R.layout.fragment_enter_category_title) {

    private val categoryTitleList = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNetwork()
        initClickListener()
        setTextWatcher()
        setKeyBoardUp()
        toolbarClickListener()
        setUpAsSoftKeyboard(view) // 다음버튼 위/아래 움직이게
    }

    private fun initNetwork() {
        lifecycleScope.launch {
            kotlin.runCatching {
                val response =
                    RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                        .getAllCategories().data ?: emptyList()
                for (element in response) categoryTitleList += element.title
            }
        }
    }

    private fun setTextWatcher() {
        binding.etCategoryTitle.addTextChangedListener {
            val title = binding.etCategoryTitle.text.toString()
            binding.isDuplicated = title in categoryTitleList
        }
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(
                EnterCategoryTitleFragmentDirections.actionEnterCategoryTitleFragmentToChooseIconFragment(
                    binding.etCategoryTitle.text.toString()
                )
            )
        }

        binding.ivDeleteText.setOnClickListener { binding.etCategoryTitle.text.clear() }
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
        binding.icClose.setOnClickListener { requireActivity().finish() }
    }

    private fun setKeyBoardUp() =
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etCategoryTitle)

    companion object {
        const val TAG = "AddCategoryFragment"
    }
}
