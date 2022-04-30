package org.sopt.havit.ui.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentDialogContentsFilterBinding

class DialogContentsFilterFragment(private var contentsFilter: String) : BottomSheetDialogFragment() {
    private var _binding: FragmentDialogContentsFilterBinding? = null
    private val binding get() = _binding ?: error("Binding이 초기화 되지 않았습니다.")
    private lateinit var filterClickListener: OnFilterClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_contents_filter,
            container,
            false
        )
        binding.filter = contentsFilter

        clickFilter()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // radio group 클릭 시 이벤트 정의
    private fun clickFilter() {
        binding.rgContainer.setOnCheckedChangeListener { _, checkedId ->
            // checkedId가 기존 filter와 다른 경우 이벤트 실행
            // 조건문을 넣어주지 않으면 기존 checkedId = -1 에서 삼항연산자를 통해 바뀌기 때문에 show()와 동시에 함수가 실행된다
            if (binding.filter != (when (checkedId) {
                    R.id.rb_recent -> "created_at"
                    R.id.rb_past -> "reverse"
                    else -> "seen_at"
                }.also { contentsFilter = it })
            ) {
                filterClickListener.onClick(contentsFilter) // 함수 실행
            }
        }
    }

    // 콜백을 위한 인터페이스
    interface OnFilterClickListener {
        fun onClick(filter: String)
    }

    fun setFilterClickListener(onFilterClickListener: OnFilterClickListener) {
        this.filterClickListener = onFilterClickListener
    }
}
