package org.sopt.havit.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.FragmentBottomSheetNoticeCommunityBinding
import org.sopt.havit.util.HavitSharedPreference
import org.sopt.havit.util.setOnSingleClickListener
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetNoticeCommunityFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetNoticeCommunityBinding? = null
    private val binding get() = _binding ?: error("Binding이 초기화 되지 않았습니다.")
    private lateinit var startCommunityClickListener: OnStartCommunityClickListener

    @Inject
    lateinit var preference: HavitSharedPreference

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

    override fun dismiss() {
        super.dismiss()
        setNoticeCommunityNeverWatch()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog) // back 키, 다이얼로그 외부 영역 눌렀을 때 동작
        setNoticeCommunityNeverWatch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.tvClose.setOnSingleClickListener {
            dismiss()
        }

        binding.tvCommunityShortcut.setOnSingleClickListener {
            startCommunityClickListener.onClick()
            dismiss()
        }
    }

    private fun setNoticeCommunityNeverWatch() {
        if (binding.cbNeverWatch.isChecked) {
            preference.setNoticeCommunityNeverWatch()
        }
    }

    fun setStartCommunityClickListener(onStartCommunityClickListener: OnStartCommunityClickListener) {
        this.startCommunityClickListener = onStartCommunityClickListener
    }

    companion object {
        const val TAG = "BottomSheetNoticeCommunityFragment"
    }
}