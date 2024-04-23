package org.sopt.havit.ui.home.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentCommunityBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import kotlin.math.roundToInt

@AndroidEntryPoint
class CommunityFragment :
    BaseBindingFragment<FragmentCommunityBinding>(R.layout.fragment_community) {
    private val viewModel: CommunityViewModel by viewModels()
    private val adapter by lazy {
        CommunityPagingDataAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
    }

    private fun initView() {
        binding.rvCommunity.adapter = adapter
        val decoration = DividerItemDecoration(requireActivity(), VERTICAL)
        binding.rvCommunity.addItemDecoration(decoration)
    }

    private fun observe() {
        // 카테고리 chip 동적 생성
        viewModel.communityCategoryList.observe(viewLifecycleOwner) { list ->
            list.forEachIndexed { index, value ->
                val chip = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_chip, binding.cgCommunityCategory, false) as Chip

                if (list.lastIndex == index) { // 마지막 chip marginEnd 16dp
                    val density = requireContext().resources.displayMetrics.density
                    val param = chip.layoutParams as ViewGroup.MarginLayoutParams
                    param.marginEnd = (16.toFloat() * density).roundToInt()
                    chip.layoutParams = param
                }

                with(chip) {
                    text = value.name
                    setOnClickListener {
                        //조회 로직
                    }
                }

                binding.cgCommunityCategory.addView(chip)
            }
        }

        // 카테고리 리스트 전체 조회
        lifecycleScope.launch {
            viewModel.getCommunityAllPosts().collect { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }
    }
}