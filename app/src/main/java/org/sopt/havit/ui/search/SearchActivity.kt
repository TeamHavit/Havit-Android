package org.sopt.havit.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import org.koin.android.viewmodel.ext.android.viewModel
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySearchBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.util.KeyBoardUtil


class SearchActivity : BaseBindingActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val searchViewModel: SearchViewModel by viewModel()
    private val searchContentsAdapter: SearchContentsAdapter by lazy {
        SearchContentsAdapter(
            searchViewModel,
            supportFragmentManager
        )
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.getSearchContents(binding.etSearch.text.toString())
        observers()
        setAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOpenKeyBoard()
        setContentView(binding.root)
        binding.vm = searchViewModel
        setListeners()
        setOpenKeyBoard()
    }

    private fun setOpenKeyBoard() {
        KeyBoardUtil.openKeyBoard(this, binding.etSearch)
    }


    private fun setAdapter() {
        binding.rvSearch.adapter = searchContentsAdapter
        //searchContentsAdapter.notifyDataSetChanged()
    }

    private fun setListeners() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(c: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (searchViewModel.isSearchFirst.value == false) {
                    if (binding.etSearch.text.isNotEmpty()) {
                        searchViewModel.setSearchNoImage(false)
                        searchViewModel.setSearchNoText(true)
                        searchViewModel.setSearchImage(true)
                    } else {
                        searchViewModel.setSearchNoImage(false)
                        searchViewModel.setSearchNoText(false)
                        searchViewModel.setSearchImage(false)
                    }
                }


            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.etSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
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

    private fun setCustomToast() {
        val toast = Toast(this)
        val view = layoutInflater.inflate(R.layout.toast_havit_complete, null)
        toast.view = view
        toast.show()
    }

    private fun observers() {
        searchViewModel.searchResult.observe(this) {
            binding.tvSearchCount.text = it.size.toString()
            if (it.isNullOrEmpty()) { // 데이터 없음
                searchViewModel.setSearchNoText(false)
                searchViewModel.setSearchNoImage(false)
                binding.rvSearch.visibility = GONE
                searchViewModel.isSearchFirst.value = false
            } else {
                searchContentsAdapter.setItem(it)
                searchViewModel.isSearchFirst.value = true
                searchViewModel.setSearchNoText(true)
                searchViewModel.setSearchNoImage(true)
                binding.rvSearch.isVisible = true

            }
        }
        searchViewModel.isRead.observe(this) {
            if (it) {
                setCustomToast()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        searchViewModel.getSearchContents(binding.etSearch.text.toString())
    }


}