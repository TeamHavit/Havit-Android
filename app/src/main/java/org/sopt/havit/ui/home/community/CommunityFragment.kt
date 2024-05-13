package org.sopt.havit.ui.home.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentCommunityBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.community.CreatePostActivity
import org.sopt.havit.util.setOnSingleClickListener

@AndroidEntryPoint
class CommunityFragment :
    BaseBindingFragment<FragmentCommunityBinding>(R.layout.fragment_community) {
    private val viewModel: CommunityViewModel by viewModels()
    private val adapter by lazy {
        CommunityPagingDataAdapter(
            onSettingClick = { id -> showReportDialog(id) },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
        getCommunityAllPosts()
    }

    private fun initView() {
        binding.rvCommunity.adapter = adapter

        binding.chAll.setOnSingleClickListener {
            getCommunityAllPosts()
            adapter.refresh() // 새로 데이터를 받아오기 위해
        }

        onPostButtonClick()
    }

    private fun observe() {
        // 카테고리 chip 동적 생성
        viewModel.communityCategoryList.observe(viewLifecycleOwner) { list ->
            list.forEachIndexed { _, value ->
                val chip = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_chip, binding.cgCommunityCategory, false) as Chip

                with(chip) {
                    text = value.name
                    setOnSingleClickListener {
                        getCommunityPostsByCategoryWithRefresh(value.id)
                    }
                }

                binding.cgCommunityCategory.addView(chip)
            }
        }
    }

    private fun getCommunityAllPosts() {
        lifecycleScope.launch {
            viewModel.getCommunityAllPosts().collect { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }
    }

    private fun getCommunityPostsByCategoryWithRefresh(categoryId: Int) {
        lifecycleScope.launch {
            viewModel.getCommunityPostsByCategory(categoryId).collect { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }
        adapter.refresh() // 새로 데이터를 받아오기 위해
    }

    private fun showReportDialog(id: Int) {
        val bottomSheet = BottomSheetReportFragment()
        bottomSheet.show(childFragmentManager, BottomSheetReportFragment.TAG)

        bottomSheet.setReportClickListener(
            object : BottomSheetReportFragment.OnReportClickListener {
                override fun onClick() {
                    viewModel.postCommunityReport(id)
                    bottomSheet.dismiss()
                    adapter.refresh()
                }
            })
    }

    private fun onPostButtonClick() {
        binding.ivCreateCommunityContents.setOnSingleClickListener {
            moveToCreatePostActivity()
        }
    }

    private fun moveToCreatePostActivity() {
        val intent = Intent(requireContext(), CreatePostActivity::class.java)
        startActivity(intent)

    }
}