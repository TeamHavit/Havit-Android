package org.sopt.havit.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.DialogOkUtilBinding

class DialogOkUtil(private val dialogMode: Int, private val doAfterConfirm: () -> Unit) :
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
    }

    private fun setLayout(view: View) {
        view.layoutParams.width = (resources.displayMetrics.widthPixels * 0.88).toInt()
    }

    private fun setTitle() {
        binding.title = when (dialogMode) {
            UNREGISTER -> getString(R.string.unregister_complete)
            else -> throw IllegalStateException()
        }
    }

    private fun setDescription() {
        binding.description = when (dialogMode) {
            UNREGISTER -> getString(R.string.unregister_complete_description)
            else -> throw IllegalStateException()
        }
    }

    private fun setConfirmText() {
        binding.confirmText = when (dialogMode) {
            UNREGISTER -> getString(R.string.check)
            else -> throw IllegalStateException()
        }
    }

    private fun setConfirmBackground() {
        when (dialogMode) {
            UNREGISTER -> binding.btnConfirm.setBackgroundResource(R.drawable.rectangle_havit_gray_bottom_radius_8)
            else -> throw IllegalStateException()
        }
    }

    private fun clickListener() {
        binding.ivClose.setOnSingleClickListener {
            dismiss()
            doAfterConfirm()
        }
        binding.btnConfirm.setOnSinglePostClickListener {
            dismiss()
            doAfterConfirm()
        }
    }

    override fun onDestroyView() {
        if (dialogMode == UNREGISTER) {
            doAfterConfirm()
        }
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val UNREGISTER = 0
    }

}