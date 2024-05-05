package org.sopt.havit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.databinding.FragmentBottomSheetNoticeCommunityBinding
import org.sopt.havit.util.setOnSingleClickListener

class BottomSheetNoticeCommunityFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetNoticeCommunityBinding? = null
    private val binding get() = _binding ?: error("Binding이 초기화 되지 않았습니다.")
    private lateinit var startCommunityClickListener: OnStartCommunityClickListener

    interface OnStartCommunityClickListener {
        fun onClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetNoticeCommunityBinding.inflate(inflater, container, false)

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
        binding.tvClose.setOnSingleClickListener {
            dismiss()
        }

        binding.tvCommunityShortcut.setOnSingleClickListener {
            startCommunityClickListener.onClick()
        }
    }

    fun setStartCommunityClickListener(onStartCommunityClickListener: OnStartCommunityClickListener) {
        this.startCommunityClickListener = onStartCommunityClickListener
    }

    companion object {
        const val TAG = "BottomSheetNoticeCommunityFragment"
    }
}