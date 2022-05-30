package org.sopt.havit.ui.contents.more.edit_title

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentEditTitleBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment.Companion.CONTENTS_DATA

class EditTitleFromMoreFragment :
    BaseBindingFragment<FragmentEditTitleBinding>(R.layout.fragment_edit_title) {
    var contents: ContentsMoreData? = null
    private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBackButtonInvisible()
        initCompleteBtnClick()
        initBottomSheetDialogFragment()
        setKeyBoardUp()
        setOriginTitle()
        setCursor()
    }

    private fun getBundleData() {
        arguments?.let {
            contents = it.getParcelable(CONTENTS_DATA)
        }
    }

    private fun setOriginTitle() {
        val originTitle = contents?.title
        binding.etTitle.setText(originTitle)
    }

    private fun setCursor() {
        binding.etTitle.post {
            binding.etTitle.requestFocus()
            binding.etTitle.setSelection(contents?.title?.length ?: 0)
        }
    }

    private fun initBottomSheetDialogFragment() {
        bottomSheetDialogFragment = requireParentFragment() as BottomSheetDialogFragment
    }

    private fun setBackButtonInvisible() {
        binding.icBack.visibility = View.INVISIBLE
    }

    private fun initCompleteBtnClick() {
        binding.tvComplete.setOnClickListener {
            if (isTitleModified()) { /*서버통신 */
            }
            bottomSheetDialogFragment.dismiss()
        }
    }

    private fun isTitleModified() =
        binding.etTitle.text.toString() != (contents?.title ?: throw IllegalArgumentException())

    private fun setKeyBoardUp() {
        bottomSheetDialogFragment.dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        bottomSheetDialogFragment.dialog?.setOnShowListener {
            val imm =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }
}
