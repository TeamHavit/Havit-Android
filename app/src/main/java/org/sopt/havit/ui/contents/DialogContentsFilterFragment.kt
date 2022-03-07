package org.sopt.havit.ui.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentDialogContentsFilterBinding

class DialogContentsFilterFragment(private val filter: String) : BottomSheetDialogFragment() {
    private var _binding: FragmentDialogContentsFilterBinding? = null
    private val binding get() = _binding ?: error("Binding이 초기화 되지 않았습니다.")
    private lateinit var filterClickListener: OnFilterClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogContentsFilterBinding.inflate(layoutInflater, container, false)

        initChecked()
        clickFilter()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // 기존 filter의 check 속성 및 디자인 설정
    private fun initChecked() {
        with(binding) {
            when (filter) {
                "created_at" -> {
                    rbRecent.isChecked = true
                    // 위 아래 선 제거
                    vTop.visibility = INVISIBLE
                    vRecent.visibility = INVISIBLE
                }
                "reverse" -> {
                    rbPast.isChecked = true
                    vRecent.visibility = INVISIBLE
                    vPast.visibility = INVISIBLE
                }
                else -> {
                    rbView.isChecked = true
                    vPast.visibility = INVISIBLE
                    vView.visibility = INVISIBLE
                }
            }
        }
    }

    // radio group 클릭 시 이벤트 정의
    private fun clickFilter() {
        binding.rgContainer.setOnCheckedChangeListener { _, checkedId ->
            filterClickListener.onClick(
                when (checkedId) {
                    R.id.rb_recent -> "created_at"
                    R.id.rb_past -> "reverse"
                    else -> "seen_at"
                }
            )
        }
    }

    interface OnFilterClickListener {
        fun onClick(filter: String)
    }

    fun setFilterClickListener(onFilterClickListener: OnFilterClickListener) {
        this.filterClickListener = onFilterClickListener
    }
}
