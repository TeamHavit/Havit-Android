package org.sopt.havit.ui.home.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.databinding.FragmentBottomSheetReportBinding
import org.sopt.havit.util.setOnSingleClickListener

class BottomSheetReportFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetReportBinding? = null
    private val binding get() = _binding ?: error("Binding이 초기화 되지 않았습니다.")
    private lateinit var reportClickListener: OnReportClickListener

    interface OnReportClickListener {
        fun onClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetReportBinding.inflate(inflater, container, false)

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

        binding.tvReport.setOnSingleClickListener {
            reportClickListener.onClick()
        }
    }

    fun setReportClickListener(onReportClickListener: OnReportClickListener) {
        this.reportClickListener = onReportClickListener
    }

    companion object {
        const val TAG = "BottomSheetReportFragment"
    }
}