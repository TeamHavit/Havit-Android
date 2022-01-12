package org.sopt.havit.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import org.koin.android.viewmodel.ext.android.viewModel
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSearchBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.KeyBoardUtil


class SearchFragment : BaseBindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyBoardUtil.openKeyBoard(requireContext(), binding.etSearch)
    }

    private fun setListeners() {
        binding.etSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                //searchViewModel.getSearchContents(text.toString()
                Log.d("asdf", binding.etSearch.text.toString())
                KeyBoardUtil.hideKeyBoard(requireActivity())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }


}