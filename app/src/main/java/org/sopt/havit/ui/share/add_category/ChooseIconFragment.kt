package org.sopt.havit.ui.share.add_category

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryAddRequest
import org.sopt.havit.databinding.FragmentChooseIconBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.share.add_category.IconAdapter.Companion.clickedPosition
import org.sopt.havit.util.ADD_CATEGORY_TOP_TYPE
import org.sopt.havit.util.MySharedPreference
import org.sopt.havit.util.ToastUtil

@AndroidEntryPoint
class ChooseIconFragment :
    BaseBindingFragment<FragmentChooseIconBinding>(R.layout.fragment_choose_icon) {
    private lateinit var iconAdapter: IconAdapter
    private val args by navArgs<ChooseIconFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initClickNext()
        toolbarClickListener()
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
        binding.icClose.setOnClickListener { requireActivity().finish() }
    }

    private fun initAdapter() {
        clickedPosition = 0 // navigation 백스택 관리코드 작성하면 지우기
        binding.rvIcon.adapter = IconAdapter(::onIconClick).also { iconAdapter = it }
    }

    private fun onIconClick(position: Int) {
        val previousPosition = clickedPosition
        clickedPosition = position
        iconAdapter.notifyItemChanged(previousPosition)
        iconAdapter.notifyItemChanged(clickedPosition)
    }

    private fun initClickNext() {
        binding.btnNext.setOnClickListener {
            lifecycleScope.launch {
                initNetwork()
                if (requireActivity().toString().contains("CategoryAddActivity"))
                    requireActivity().finish() // 분기처리 그지 깽깽이 같은데 오늘 자고 더 좋은방법 연구할게요...
                findNavController().navigate(R.id.action_chooseIconFragment_to_selectCategoryFragment)
            }
        }
    }

    private suspend fun initNetwork() {
        lifecycleScope.launch {
            kotlin.runCatching {
                RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                    .addCategory(CategoryAddRequest(args.categoryTitle, clickedPosition + 1))
                showCustomToast()
            }
        }.join()
    }

    private fun showCustomToast() {
        ToastUtil(requireContext()).makeToast(
            toastType = ADD_CATEGORY_TOP_TYPE,
            categoryName = args.categoryTitle
        )
    }
}
