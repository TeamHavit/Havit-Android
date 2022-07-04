package org.sopt.havit.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
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
import java.io.Serializable

@AndroidEntryPoint
class SearchActivity : BaseBindingActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val searchViewModel: SearchViewModel by viewModels()
    private val searchContentsAdapter: SearchContentsAdapter by lazy { SearchContentsAdapter() }
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

    private fun setOpenKeyBoard() {
        KeyBoardUtil.openKeyBoard(this, binding.etSearch)
    }

    private fun setAdapter() {
        binding.rvSearch.adapter = searchContentsAdapter
    }

    private fun setListeners() {
        binding.etSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (categoryId.isEmpty()) searchViewModel.getSearchContents(binding.etSearch.text.toString())
                else searchViewModel.getSearchContentsInCategories(
                    categoryId,
                    binding.etSearch.text.toString()
                )
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
                override fun onSettingClick(v: View, data: Contents, pos: Int) {
                    val removeItem: (Int) -> Unit = {
                        searchContentsAdapter.notifyItemRemoved(it)
                        searchContentsAdapter.searchContents.removeAt(it)
                        //searchViewModel.searchResultSize.value -= 1
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

                    val bundle = setBundle(dataMore, removeItem, pos)
                    val dialog = ContentsMoreFragment()
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "setting")
                }
            })
            setItemClickListener(object : SearchContentsAdapter.OnItemClickListener {
                override fun onClick(v: View, data: Contents) {

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
                    data: Contents,
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

    // ContentsMoreFragment에 보낼 bundle 생성
    private fun setBundle(
        dataMore: ContentsMoreData?,
        removeItem: (Int) -> Unit,
        position: Int
    ): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(ContentsMoreFragment.CONTENTS_MORE_DATA, dataMore)
        bundle.putSerializable(ContentsMoreFragment.REMOVE_ITEM, removeItem as Serializable)
        bundle.putInt(ContentsMoreFragment.POSITION, position)
        return bundle
    }

    private fun setCustomToast() {
        val toast = Toast(this)
        val view = layoutInflater.inflate(R.layout.toast_havit_complete, null)
        toast.view = view
        toast.show()
    }

    private fun observers() {
        searchViewModel.searchReload.observe(this) {
            if (categoryId.isEmpty()) searchViewModel.getSearchContents(binding.etSearch.text.toString())
            else searchViewModel.getSearchContentsInCategories(
                categoryId,
                binding.etSearch.text.toString()
            )
        }
        searchViewModel.searchResult.observe(this) {
            if (!it.isNullOrEmpty()) {
                searchContentsAdapter.setItem(it)
                searchViewModel.searchTv.value = false
            } else {
                searchViewModel.searchTv.value = true
            }
        }
        searchViewModel.isRead.observe(this) {
            if (it) {
                setCustomToast()
            }
        }
    }
}
