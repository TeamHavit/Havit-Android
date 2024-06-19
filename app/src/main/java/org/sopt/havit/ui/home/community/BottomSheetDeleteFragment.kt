package org.sopt.havit.ui.home.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.databinding.FragmentBottomSheetDeleteBinding
import org.sopt.havit.util.setOnSingleClickListener

class BottomSheetDeleteFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetDeleteBinding? = null
    private val binding get() = _binding ?: error("Binding이 초기화 되지 않았습니다.")
    private lateinit var deleteClickListener: OnDeleteClickListener

    interface OnDeleteClickListener {
        fun onClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDeleteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() {
        binding.tvCancel.setOnSingleClickListener {
            dismiss()
        }

        binding.tvDelete.setOnSingleClickListener {
            deleteClickListener.onClick()
        }
    }

    fun setDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.deleteClickListener = onDeleteClickListener
    }

    companion object {
        const val TAG = "BottomSheetDeleteFragment"
    }
}