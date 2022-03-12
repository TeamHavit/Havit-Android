package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryAddRequest
import org.sopt.havit.databinding.FragmentChooseIconBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.MySharedPreference

class ChooseIconFragment :
    BaseBindingFragment<FragmentChooseIconBinding>(R.layout.fragment_choose_icon) {
    private lateinit var iconAdapter: IconAdapter
    private val args by navArgs<ChooseIconFragmentArgs>()
    private var categoryIndex = 0   // default 로 첫번째 아이콘 선택

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initClickNext()
        toolbarClickListener()
    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener { findNavController().navigate(R.id.action_chooseIconFragment_to_addCategoryFragment) }
        binding.icClose.setOnClickListener { requireActivity().finish() }
    }

    private fun initAdapter() {
        binding.rvIcon.adapter = IconAdapter(::onIconClick).also { iconAdapter = it }
    }

    private fun onIconClick(position: Int) {
        categoryIndex = position
        iconAdapter.clickedPosition = position
        for (i in 0..IconAdapter.iconList.size) iconAdapter.notifyItemChanged(i)
        Log.d("ChooseIconFragment_position", position.toString())
    }


    private fun initClickNext() {
        binding.btnNext.setOnClickListener {
            initNetwork()
            findNavController().navigate(R.id.action_chooseIconFragment_to_selectCategoryFragment)
        }
    }

    private fun initNetwork() {
        lifecycleScope.launch {
            kotlin.runCatching {
                RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                    .addCategory(CategoryAddRequest(args.categoryTitle, categoryIndex + 1))
                showCustomToast()
            }
        }
    }

    private fun showCustomToast() {
        val toast = Toast(requireContext())
        val view = layoutInflater.inflate(R.layout.toast_category_added, null)
        val textView: TextView = view.findViewById(R.id.tv_toast_category1)
        textView.text = args.categoryTitle
        toast.view = view
        toast.show()
    }
}