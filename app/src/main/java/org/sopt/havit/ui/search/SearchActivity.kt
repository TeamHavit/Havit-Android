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
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.KeyBoardUtil


class SearchActivity : BaseBindingActivity<FragmentSearchBinding>(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vm=searchViewModel
        KeyBoardUtil.openKeyBoard(this, binding.etSearch)
        setListeners()
        observers()
    }

    private fun setListeners() {
        binding.etSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                //검색을 눌렀을 때 데이터가 있으면 키보드만 내려가
                searchViewModel.getSearchContents(binding.etSearch.text.toString())
                    // 없으면?

                Log.d("asdf", binding.etSearch.text.toString())


                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun observers(){
        searchViewModel.searchResult.observe(this){
            binding.tvSearchCount.text=it.size.toString()
            if(it.isEmpty()){

            }else{
                KeyBoardUtil.hideKeyBoard(this)
            }
        }
    }


}