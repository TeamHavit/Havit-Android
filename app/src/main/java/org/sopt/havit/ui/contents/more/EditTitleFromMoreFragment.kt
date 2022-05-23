package org.sopt.havit.ui.contents.more

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentEditTitleBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.OnBackPressedHandler

class EditTitleFromMoreFragment() :
    BaseBindingFragment<FragmentEditTitleBinding>(R.layout.fragment_edit_title),
    OnBackPressedHandler {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        setOriginTitle()
        setKeyBoardUp()
        setBackButtonInvisible()
        initCompleteBtnClick()
    }

    private fun setBackButtonInvisible() {
        binding.icBack.visibility = View.INVISIBLE
    }

    private fun initCompleteBtnClick() {
        binding.tvComplete.setOnClickListener {
            Toast.makeText(requireContext(), "pressed", Toast.LENGTH_SHORT).show()
            val tmp = requireParentFragment() as BottomSheetDialogFragment
            tmp.dismiss()
        }
    }

//    private fun setOriginTitle() = binding.etTitle.setText(originTitle)

    private fun setKeyBoardUp() = KeyBoardUtil.openKeyBoard(requireContext(), binding.etTitle)

    override fun onBackPressed(): Boolean {
        return true
    }
}
