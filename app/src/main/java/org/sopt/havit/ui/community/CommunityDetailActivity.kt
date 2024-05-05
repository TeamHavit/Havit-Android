package org.sopt.havit.ui.community

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCommunityDetailBinding
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.ui.share.ShareActivity
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.setOnSingleClickListener


@AndroidEntryPoint
class CommunityDetailActivity :
    BaseActivity<ActivityCommunityDetailBinding>(R.layout.activity_community_detail) {

    private val communityDetailViewModel: CommunityDetailViewModel by viewModels<CommunityDetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()
        initView()
        setListeners()
    }

    private fun bindViewModel() {
        binding.vm = communityDetailViewModel
    }

    private fun initView() {
        val id = intent?.getIntExtra("communityPostId", -1)
        fetchCommunityPostDetailWithId(id)
    }

    private fun fetchCommunityPostDetailWithId(id: Int?) {
        id?.let {
            communityDetailViewModel.getCommunityPostDetail(it)
        }
    }

    private fun setListeners() {
        binding.clUrl.setOnSingleClickListener {
            communityDetailViewModel.communityPost.value?.contentUrl?.let {
                startWebActivityWithCaller(
                    requireNotNull(CommunityDetailActivity::class.simpleName)
                )
            }
        }
        binding.btnBack.setOnSingleClickListener {
            finish()
        }
        binding.btnSave.setOnSingleClickListener {
            startShareActivity()
            finish()
        }

    }

    private fun startWebActivityWithCaller(callerClassName: String) {
        val intent = Intent(this, WebActivity::class.java).apply {
            putExtra("url", communityDetailViewModel.communityPost.value?.contentUrl)
            putExtra("caller", callerClassName)
        }
        startActivity(intent)
    }

    private fun startShareActivity() {
        val intent = Intent(this, ShareActivity::class.java).apply {
            putExtra("url", communityDetailViewModel.communityPost.value?.contentUrl)
        }
        startActivity(intent)
    }


}
