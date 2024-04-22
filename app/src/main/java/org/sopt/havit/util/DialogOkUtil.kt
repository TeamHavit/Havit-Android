package org.sopt.havit.util

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.DialogOkUtilBinding

class DialogOkUtil(
    private val dialogMode: Int,
    private val doAfterConfirm: () -> Unit,
    private val dismissAvailability: Boolean = true,
) :
    DialogFragment() {
    private var _binding: DialogOkUtilBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.DisableDialogBackground)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = DialogOkUtilBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout(view)
        setTitle()
        setDescription()
        setConfirmText()
        setConfirmBackground()
        clickListener()
        setDismissUnable()
    }

    private fun setLayout(view: View) {
        view.layoutParams.width = (resources.displayMetrics.widthPixels * 0.88).toInt()
    }

    private fun setTitle() {
        binding.title = when (dialogMode) {
            UNREGISTER -> getString(R.string.unregister_complete)
            FORCED_UPDATE -> getString(R.string.update_notification)
            else -> ""
        }
    }

    private fun setDescription() {
        binding.description = when (dialogMode) {
            UNREGISTER -> getString(R.string.unregister_complete_description)
            FORCED_UPDATE -> getString(R.string.update_notification_description)
            else -> ""
        }
    }

    private fun setConfirmText() {
        binding.confirmText = when (dialogMode) {
            UNREGISTER -> getString(R.string.check)
            FORCED_UPDATE -> getString(R.string.update)
            else -> ""
        }
    }

    private fun setConfirmBackground() {
        val res = when (dialogMode) {
            UNREGISTER -> R.drawable.rectangle_havit_gray_bottom_radius_8
            FORCED_UPDATE -> R.drawable.rectangle_havit_purple_bottom_radius_8
            else -> R.drawable.rectangle_havit_gray_bottom_radius_8
        }
        binding.btnConfirm.setBackgroundResource(res)
    }

    private fun clickListener() {
        binding.ivClose.setOnSingleClickListener { dismiss() }
        binding.btnConfirm.setOnSinglePostClickListener {
            doAfterConfirm()
            dismiss()
        }
    }

    private fun setDismissUnable() {
        if (!dismissAvailability) {
            binding.ivClose.visibility = View.GONE
            isCancelable = false
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        if (dismissAvailability) {
            super.onCancel(dialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val UNREGISTER = 0
        const val FORCED_UPDATE = 1
    }

}