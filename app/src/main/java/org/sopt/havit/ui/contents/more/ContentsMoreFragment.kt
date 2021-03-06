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

class ContentsMoreFragment :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentContentsMoreBinding
    private lateinit var contentsData: ContentsMoreData
    private lateinit var showDeleteDialog: () -> Unit
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
        BottomSheetMoreFragment.newInstance(viewType, contentsData)
            .show(parentFragmentManager, this.tag)
        dismiss()
    }

    private fun initModifyTitleClick() {
        binding.clEditTitle.setOnClickListener {
            viewType = Edit_TITLE
            startBottomSheetWithDesignatedView()
        }
    }

    private fun initMoveCategoryClick() {
        binding.clMoveCategory.setOnClickListener {
            viewType = MOVE_CATEGORY
            startBottomSheetWithDesignatedView()
        }
    }

    private fun initSetAlarm() {
        binding.clSetAlarm.setOnClickListener {
            viewType = SET_ALARM
            startBottomSheetWithDesignatedView()
        }
    }

    // ?????? ????????? bundle??? ?????? content ????????? ????????????
    private fun setData() {
        contentsData = arguments?.getParcelable(CONTENTS_MORE_DATA) ?: throw IllegalStateException()
        showDeleteDialog = arguments?.getSerializable(SHOW_DELETE_DIALOG) as () -> Unit
        arguments?.getInt(POSITION, 0)?.let {
            pos = it
        }

        binding.data = contentsData
    }

    private fun initShareClick() {
        binding.clEditShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, contentsData.url)
                type = "text/html"
            }
            startActivity(Intent.createChooser(intent, null))
            dismiss()
        }
    }

    // ????????? ?????? ?????? ?????? ??? ?????? ??????
    private fun clickDelete() {
        binding.clEditDelete.setOnClickListener {
            showDeleteDialog()
            dismiss()
        }
    }

    companion object {
        const val CONTENTS_MORE_DATA = "ContentsMoreData"
        const val SHOW_DELETE_DIALOG = "showDeleteDialog"
        const val POSITION = "position"
    }
}
