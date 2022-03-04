package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.databinding.FragmentAddCategoryBinding
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard
import org.sopt.havit.util.MySharedPreference

class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!
    private val categoryTitleList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

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
                        .getAllCategory()
                val categoryData = response.data

                for (element in categoryData)
                    categoryTitleList += element.title
            }
        }
    }

    private fun setTextWatcher() {
        binding.etCategoryTitle.addTextChangedListener {
            setTextCount()
            if (isTitleNull()) setBtnColor(false)
            else checkDuplicateCategory(binding.etCategoryTitle.text.toString())
        }
    }

    private fun checkDuplicateCategory(title: String?) {
        // 중복 된 카테고리가 있는지 검사
        var isDuplicated = false
        for (element in categoryTitleList) {
            if (element == title) {
                isDuplicated = true
                if (isDuplicated) break
            }
        }
        // 중복 카테고리 여부에 따른 UI 설정(중복 warning & 다음 버튼 색)
        if (isDuplicated) binding.clDuplicateCategory.visibility = View.VISIBLE
        else binding.clDuplicateCategory.visibility              = View.INVISIBLE
        setBtnColor(!isDuplicated)
    }

    private fun setBtnColor(isEnabled: Boolean) {
        if (!isEnabled) {
            binding.btnNext.setBackgroundColor(resources.getColor(R.color.gray_2))
            binding.btnNext.isEnabled = false
        } else {
            binding.btnNext.setBackgroundColor(resources.getColor(R.color.havit_purple))
            binding.btnNext.isEnabled = true
        }
    }

    private fun setTextCount() {
        binding.tvCategoryLengthCount.text = binding.etCategoryTitle.text.length.toString()
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(
                AddCategoryFragmentDirections.actionAddCategoryFragmentToChooseIconFragment(
                    binding.etCategoryTitle.text.toString()
                )
            )
        }

        binding.ivDeleteText.setOnClickListener {
            binding.etCategoryTitle.text.clear()
        }
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_addCategoryFragment_to_selectCategoryFragment)
        }

        binding.icClose.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun isTitleNull(): Boolean = binding.etCategoryTitle.text.isEmpty()

    private fun setKeyBoardUp() =
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etCategoryTitle)

    companion object {
        const val TAG = "AddCategoryFragment"
    }
}