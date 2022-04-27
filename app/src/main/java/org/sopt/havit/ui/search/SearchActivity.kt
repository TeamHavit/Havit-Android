package org.sopt.havit.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.ActivitySearchBinding
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.contents.ContentsMoreFragment
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.KeyBoardUtil

@AndroidEntryPoint
class SearchActivity : BaseBindingActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val searchViewModel: SearchViewModel by viewModels()
    private val searchContentsAdapter: SearchContentsAdapter by lazy { SearchContentsAdapter() }
    private var isRead = false
    private val categoryId: String by lazy {
        intent.getStringExtra("categoryName").toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOpenKeyBoard()
        setContentView(binding.root)
        binding.vm = searchViewModel
        setAdapter()
        setListeners()
        setOpenKeyBoard()
        observers()
    }

    private fun setSearchContents() {
        searchViewModel.getSearchContents(binding.etSearch.text.toString())
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
                if (categoryId != null) {
                    searchViewModel.getSearchContentsInCategories(
                        categoryId,
                        binding.etSearch.text.toString()
                    )
                } else {
                    searchViewModel.getSearchContents(binding.etSearch.text.toString())
                }
                KeyBoardUtil.hideKeyBoard(this)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.tvSearchCancel.setOnClickListener {
            finish()
        }
        with(searchContentsAdapter) {
            setItemSettingClickListener(object : SearchContentsAdapter.OnItemSettingClickListener {
                override fun onSettingClick(v: View, data: Contents.Data, pos: Int) {
                    val removeItem: (Int) -> Unit = {
                        searchContentsAdapter.notifyItemRemoved(it)
                    }
                    val dataMore = ContentsMoreData(
                        data.id,
                        data.image,
                        data.title,
                        data.createdAt,
                        data.url,
                        data.isNotified,
                        data.notificationTime
                    )
                    ContentsMoreFragment(dataMore, removeItem, pos).show(
                        supportFragmentManager,
                        "setting"
                    )
                }

            })
            setItemClickListener(object : SearchContentsAdapter.OnItemClickListener {
                override fun onClick(v: View, data: Contents.Data) {

                    var intent = Intent(this@SearchActivity, WebActivity::class.java).apply {
                        putExtra("url", data.url)
                        putExtra("isSeen", data.isSeen)
                        putExtra("contentsId", data.id)
                    }
                    startActivity(intent)
                }
            })
            setItemHavitClickListener(object : SearchContentsAdapter.OnItemHavitClickListener {
                override fun onHavitClick(
                    v: View,
                    data: Contents.Data,
                    pos: Int
                ) {
                    searchContentsAdapter.searchContents[pos].isSeen = !(data.isSeen)
                    searchViewModel.setIsSeen(data.id)
                    if (v.tag == "seen") {
                        Glide.with(v.context)
                            .load(R.drawable.ic_contents_unread)
                            .into(v as ImageView)
                        v.tag = "unseen"
                        searchViewModel.setHavitToast(false)
                    } else {
                        Glide.with(v.context)
                            .load(R.drawable.ic_contents_read_2)
                            .into(v as ImageView)
                        v.tag = "seen"
                        searchViewModel.setHavitToast(true)
                    }
                }

            })
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

}