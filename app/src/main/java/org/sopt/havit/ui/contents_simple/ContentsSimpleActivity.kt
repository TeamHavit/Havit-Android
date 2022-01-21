package org.sopt.havit.ui.contents_simple

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityContentsSimpleBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.web.WebActivity

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
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun decorationView() {
        binding.rvContents.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun initAdapter() {
        contentsAdapter = ContentsSimpleRvAdapter(contentsViewModel,supportFragmentManager)
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

    private fun clickItemView() {
        contentsAdapter.setItemClickListener(object :
            ContentsSimpleRvAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                val intent = Intent(v.context, WebActivity::class.java)
                contentsViewModel.contentsList.value?.get(position)
                    ?.let {
                        intent.putExtra("url", it.url)
                        intent.putExtra("contentsId", it.id)
                        intent.putExtra("isSeen", it.isSeen)
                    }
                startActivity(intent)
            }
        })
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
                        binding.tvAddContents.setOnClickListener {
                            SaveFragment("").show(supportFragmentManager, "save")
                        }
                    } else {
                        binding.clContentsEmpty.visibility = View.GONE
                        val min = if (data.size < 20) data.size else 20
                        val list = data.subList(0, min)
                        clickItemView()
                        contentsAdapter.contentsList.addAll(list)
                        contentsAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}