package org.sopt.havit.ui.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.FragmentContentsMoreBinding

class ContentsMoreFragment(
    contents: ContentsSearchResponse.Data,
    removeItem: (Int) -> Unit,
    position: Int
) :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentContentsMoreBinding
    private val contentsViewModel: ContentsViewModel by lazy { ContentsViewModel(requireContext()) }
    private var data = contents
    private val notifyItemRemoved = removeItem
    private val pos = position

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContentsMoreBinding.inflate(layoutInflater, container, false)
        binding.vm = contentsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setMoreView()
        deleteContents()
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private fun setMoreView() {
        contentsViewModel.setContentsView(data)
    }

    // 콘텐츠 삭제
    private fun deleteContents() {
        binding.clEditDelete.setOnClickListener {
            // 콘텐츠 삭제 함수 호출
            with(contentsViewModel) {
                requestContentsDelete(data.id)
            }
            // 각 어댑터의 notifyItemRemoved(position) 수행
            notifyItemRemoved(pos)
            // ContentsMoreFragment 삭제
            this.dismiss()
        }
    }
}