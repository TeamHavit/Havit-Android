package org.sopt.havit.ui.contents

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentContentsMoreBinding
import org.sopt.havit.util.DialogUtil

class ContentsMoreFragment :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentContentsMoreBinding
    private val contentsViewModel: ContentsViewModel by lazy { ContentsViewModel(requireContext()) }
    private lateinit var contentsData: ContentsMoreData
    private lateinit var notifyItemRemoved: (Int) -> Unit
    private var pos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContentsMoreBinding.inflate(layoutInflater, container, false)
        binding.vm = contentsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setData()
        setMoreView()
        clickDelete()
        initShareClick()
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    // 이전 뷰에서 bundle로 보낸 content 데이터 받아오기
    private fun setData() {
        contentsData = arguments?.getParcelable(CONTENTS_MORE_DATA)!!
        notifyItemRemoved = arguments?.getSerializable(REMOVE_ITEM) as (Int) -> Unit
        arguments?.getInt(POSITION, 0)?.let {
            pos = it
        }
    }

    private fun setMoreView() {
        contentsViewModel.setContentsView(contentsData)
    }

    private fun initShareClick() {
        binding.clEditShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, contentsData.url)
                type = "text/html"
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }

    // 콘텐츠 삭제 버튼 클릭 시 동작 정의
    private fun clickDelete() {
        binding.clEditDelete.setOnClickListener {
            val dialog = DialogUtil(DialogUtil.REMOVE_CONTENTS, ::deleteContents)
            dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
        }
    }

    // 콘텐츠 삭제 함수
    private fun deleteContents() {
        // 콘텐츠 삭제 서버에 요청
        with(contentsViewModel) {
            requestContentsDelete(contentsData.id)
        }
        // 각 어댑터의 notifyItemRemoved(position) 수행
        notifyItemRemoved(pos)
        // ContentsMoreFragment 삭제
        dismiss()
    }

    companion object {
        const val CONTENTS_MORE_DATA = "ContentsMoreData"
        const val REMOVE_ITEM = "removeItem"
        const val POSITION = "position"
    }
}
