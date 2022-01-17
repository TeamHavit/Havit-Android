package org.sopt.havit.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import org.koin.android.viewmodel.ext.android.viewModel
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySearchBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.util.KeyBoardUtil


class SearchActivity : BaseBindingActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setListeners()
        observers()
    }

    private fun setListeners() {
        binding.etSearch.addTextChangedListener ( object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.etSearch.text.isEmpty()){
                    searchViewModel.setSearchNoImage(false)
                    searchViewModel.setSearchNoText(false)
                    searchViewModel.setSearchImage(false)
                }else {
                    searchViewModel.setSearchNoImage(false)
                    searchViewModel.setSearchNoText(true)
                    searchViewModel.setSearchImage(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.etSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchViewModel.setSearchNoImage(false)
                searchViewModel.getSearchContents(binding.etSearch.text.toString())

                Log.d("search", binding.etSearch.text.toString())
                KeyBoardUtil.hideKeyBoard(this)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.tvSearchCancel.setOnClickListener {
            finish()
        }
    }

    private fun observers() {
        searchViewModel.searchResult.observe(this) {
            binding.tvSearchCount.text = it.size.toString()
            if (it.isEmpty()) {
                searchViewModel.setSearchNoText(false)
                searchViewModel.setSearchNoImage(false)

            } else {
                searchViewModel.setSearchNoText(true)
                searchViewModel.setSearchNoImage(true)

            }
        }
    }


}