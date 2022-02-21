package org.sopt.havit.ui.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.databinding.FragmentContentsMoreBinding


class ContentsMoreFragment(contents: ContentsSearchResponse.Data) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentContentsMoreBinding
    private val contentsViewModel: ContentsViewModel by lazy { ContentsViewModel(requireContext()) }

    private var data = contents

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
    private fun deleteContents(){
        binding.clEditDelete.setOnClickListener {
            // 콘텐츠 삭제 함수 호출
            with(contentsViewModel){
                requestContentsDelete(data.id)
                // 콘텐츠 삭제 완료 시 dialog dismiss
                deleteDelay.observe(viewLifecycleOwner){
                    if(it){dismiss()}
                }
            }
        }
    }
}