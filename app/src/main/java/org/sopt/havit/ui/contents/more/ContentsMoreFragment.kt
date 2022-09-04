package org.sopt.havit.ui.contents.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.FragmentContentsMoreBinding
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment.Companion.Edit_TITLE
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment.Companion.MOVE_CATEGORY
import org.sopt.havit.ui.contents.more.BottomSheetMoreFragment.Companion.SET_ALARM
import org.sopt.havit.util.setOnSingleClickListener

class ContentsMoreFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentContentsMoreBinding
    private lateinit var contentsData: ContentsMoreData
    private lateinit var showDeleteDialog: () -> Unit
    private lateinit var refreshData: () -> Unit
    private var pos = 0
    private lateinit var viewType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContentsMoreBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setData()
        clickDelete()
        initShareClick()
        initModifyTitleClick()
        initMoveCategoryClick()
        initSetAlarm()

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private fun startBottomSheetWithDesignatedView() {
        BottomSheetMoreFragment.newInstance(viewType, contentsData, refreshData)
            .show(parentFragmentManager, this.tag)
        dismiss()
    }

    private fun initModifyTitleClick() {
        binding.clEditTitle.setOnSingleClickListener {
            viewType = Edit_TITLE
            startBottomSheetWithDesignatedView()
        }
    }

    private fun initMoveCategoryClick() {
        binding.clMoveCategory.setOnSingleClickListener {
            viewType = MOVE_CATEGORY
            startBottomSheetWithDesignatedView()
        }
    }

    private fun initSetAlarm() {
        binding.clSetAlarm.setOnSingleClickListener {
            viewType = SET_ALARM
            startBottomSheetWithDesignatedView()
        }
    }

    // 이전 뷰에서 bundle로 보낸 content 데이터 받아오기
    private fun setData() {
        contentsData = arguments?.getParcelable(CONTENTS_MORE_DATA) ?: throw IllegalStateException()
        showDeleteDialog = arguments?.getSerializable(SHOW_DELETE_DIALOG)
        refreshData = arguments?.getSerializable(REFRESH_DATA)
        arguments?.getInt(POSITION, 0)?.let {
            pos = it
        }

        binding.data = contentsData
    }

    private fun initShareClick() {
        binding.clEditShare.setOnSingleClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, contentsData.url)
                type = "text/html"
            }
            startActivity(Intent.createChooser(intent, null))
            dismiss()
        }
    }

    // 콘텐츠 삭제 버튼 클릭 시 동작 정의
    private fun clickDelete() {
        binding.clEditDelete.setOnSingleClickListener {
            showDeleteDialog()
            dismiss()
        }
    }

    companion object {
        const val CONTENTS_MORE_DATA = "ContentsMoreData"
        const val SHOW_DELETE_DIALOG = "showDeleteDialog"
        const val REFRESH_DATA = "REFRESH_DATA"
        const val POSITION = "position"
    }
}
