package org.sopt.havit.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.DialogHavitUtilBinding

class DialogUtil(private val dialogMode: Int, private val doAfterConfirm: () -> Unit) :
    DialogFragment() {
    private var _binding: DialogHavitUtilBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.DisableDialogBackground)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = DialogHavitUtilBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout(view)
        setTitle()
        setDescription()
        setConfirmText()
        setConfirmBackground()
        clickCancelListener()
        clickConfirmListener()
    }

    private fun setLayout(view: View) {
        view.layoutParams.width = (resources.displayMetrics.widthPixels * 0.88).toInt()
    }

    private fun setTitle() {
        binding.title = when (dialogMode) {
            CANCEL_EDIT_CATEGORY -> getString(R.string.cancel_edit_category_title)
            CANCEL_SAVE_CONTENTS -> getString(R.string.cancel_save_contents_title)
            CANCEL_EDIT_TITLE -> getString(R.string.cancel_edit_title_title)
            CANCEL_SET_NOTIFICATION -> getString(R.string.cancel_set_notification_title)
            REMOVE_CATEGORY -> getString(R.string.remove_category_title)
            REMOVE_NOTIFICATION -> getString(R.string.remove_notification_title)
            REMOVE_CONTENTS -> getString(R.string.remove_contents_title)
            else -> throw IllegalStateException()
        }
    }

    private fun setDescription() {
        binding.description = when (dialogMode) {
            CANCEL_EDIT_CATEGORY -> getString(R.string.cancel_edit_category_description)
            CANCEL_SAVE_CONTENTS -> getString(R.string.cancel_save_contents_description)
            CANCEL_EDIT_TITLE -> getString(R.string.cancel_edit_title_description)
            CANCEL_SET_NOTIFICATION -> getString(R.string.cancel_set_notification_description)
            REMOVE_CATEGORY -> getString(R.string.remove_category_description)
            REMOVE_NOTIFICATION -> getString(R.string.remove_notification_description)
            REMOVE_CONTENTS -> getString(R.string.remove_contents_description)
            else -> throw IllegalStateException()
        }
    }

    private fun setConfirmText() {
        binding.confirmText = when (dialogMode) {
            CANCEL_EDIT_CATEGORY, CANCEL_SAVE_CONTENTS,
            CANCEL_EDIT_TITLE, CANCEL_SET_NOTIFICATION -> getString(R.string.exit)
            REMOVE_CATEGORY, REMOVE_NOTIFICATION, REMOVE_CONTENTS -> getString(R.string.remove)
            else -> throw IllegalStateException()
        }
    }

    private fun setConfirmBackground() {
        when (dialogMode) {
            CANCEL_EDIT_CATEGORY, CANCEL_SAVE_CONTENTS, CANCEL_EDIT_TITLE, CANCEL_SET_NOTIFICATION
            -> binding.btnConfirm.setBackgroundResource(R.drawable.rectangle_havit_gray_bottom_right_radius_8)
            REMOVE_CATEGORY, REMOVE_NOTIFICATION, REMOVE_CONTENTS
            -> binding.btnConfirm.setBackgroundResource(R.drawable.rectangle_havit_red_bottom_right_radius_8)
            else -> throw IllegalStateException()
        }
    }

    private fun clickCancelListener() {
        binding.btnCancel.setOnClickListener { dismiss() }
    }

    private fun clickConfirmListener() {
        binding.btnConfirm.setOnClickListener {
            dismiss()
            doAfterConfirm()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CANCEL_EDIT_CATEGORY = 0
        const val CANCEL_SAVE_CONTENTS = 1
        const val CANCEL_EDIT_TITLE = 2
        const val CANCEL_SET_NOTIFICATION = 3
        const val REMOVE_CATEGORY = 4
        const val REMOVE_NOTIFICATION = 5
        const val REMOVE_CONTENTS = 6
    }

}