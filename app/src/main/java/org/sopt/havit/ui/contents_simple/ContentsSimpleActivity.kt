package org.sopt.havit.ui.contents_simple

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityContentsSimpleBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class ContentsSimpleActivity :
    BaseBindingActivity<ActivityContentsSimpleBinding>(R.layout.activity_contents_simple) {

    private val contentsViewModel: ContentsSimpleViewModel by viewModels()
    private lateinit var contentsAdapter: ContentsSimpleRvAdapter
    private lateinit var contentsType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmContents = contentsViewModel

        initContents()
        initAdapter()
        dataObserve()
        decorationView()
        clickBtnBack()
    }

    private fun clickBtnBack() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun decorationView() {
        binding.rvContents.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun initAdapter() {
        contentsAdapter = ContentsSimpleRvAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun initContents() {
        intent?.let {
            it.getStringExtra("before")?.let { before ->
                contentsType = before
                setContents()
            }
        }
    }

    private fun setContents() {
        contentsViewModel.requestContentsTaken(contentsType)    // contentsList
        if (contentsType == "unseen") {// topBarName
            contentsViewModel.requestTopBarName(getString(R.string.contents_simple_unseen))
        } else {
            contentsViewModel.requestTopBarName(getString(R.string.contents_simple_recent_save))
        }
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            binding.lifecycleOwner?.let {
                contentsList.observe(it) { data ->
                    Log.d("contentsSimple", "contentsList data : $data")
                    if (data.isEmpty()) {
                        binding.rvContents.visibility = View.GONE
                        if (contentsType == "unseen")
                            requestEmptyContents(getString(R.string.contents_simple_unseen_empty))
                        else
                            requestEmptyContents(getString(R.string.contents_simple_recent_save_empty))
                    } else {
                        binding.clContentsEmpty.visibility = View.GONE
                        val min = if (data.size < 20) data.size else 20
                        val list = data.subList(0, min)
                        contentsAdapter.contentsList.addAll(list)
                        contentsAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}