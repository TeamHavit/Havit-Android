package org.sopt.havit.ui.contents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentContentsBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class ContentsFragment : BaseBindingFragment<FragmentContentsBinding>(R.layout.fragment_contents) {
    private val contentsViewModel: ContentsViewModel by viewModels()

    private var _contentsAdapter: ContentsAdapter? = null
    private val contentsAdapter get() = _contentsAdapter ?: error("adapter error")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("base", "3")
        binding.contentViewModel = contentsViewModel

        contentsViewModel.contentsList.observe(viewLifecycleOwner) {
            contentsAdapter.contentsList.addAll(it)
            contentsAdapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _contentsAdapter = null
    }
}