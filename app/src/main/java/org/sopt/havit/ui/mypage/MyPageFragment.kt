package org.sopt.havit.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentMyPageBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.contents.ContentsActivity
import org.sopt.havit.ui.setting.SettingActivity

@AndroidEntryPoint
class MyPageFragment : BaseBindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = myPageViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMyPageView()
        setListeners()
    }

    private fun setMyPageView() {
        myPageViewModel.requestUserInfo()
    }

    private fun setListeners() {
        binding.clCategoryNum.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ContentsFromMyPageActivity::class.java
                ).apply { putExtra("PageId", 1) }
            )
        }
        binding.clSavedContents.setOnClickListener {
            startActivity(
                Intent(requireContext(), ContentsActivity::class.java).apply {
                    putExtra("categoryId", ALL_CONTENTS_ID)
                    putExtra("categoryName", ALL_CONTENTS_NAME)
                }
            )
        }
        binding.clSeenContents.setOnClickListener {
            startActivity(
                Intent(requireContext(), ContentsActivity::class.java).apply {
                    putExtra("categoryId", SEEN_CONTENTS_ID)
                    putExtra("categoryName", SEEN_CONTENTS_NAME)
                }
            )
        }
        binding.ibMyPageSetting.setOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }
    }

    companion object {
        const val ALL_CONTENTS_ID = -1
        const val SEEN_CONTENTS_ID = -2
        const val ALL_CONTENTS_NAME = "?????? ?????????"
        const val SEEN_CONTENTS_NAME = "????????? ?????????"
    }
}
