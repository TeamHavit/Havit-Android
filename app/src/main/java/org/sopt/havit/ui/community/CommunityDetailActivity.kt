package org.sopt.havit.ui.community

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCommunityDetailBinding
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.ui.home.community.BottomSheetDeleteFragment
import org.sopt.havit.ui.home.community.BottomSheetReportFragment
import org.sopt.havit.ui.home.community.CommunityFragment.Companion.COMMUNITY_POST_ID
import org.sopt.havit.ui.home.community.CommunityFragment.Companion.DETAIL_COMMUNITY
import org.sopt.havit.ui.share.ShareActivity
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.CONTENT_DELETE_TYPE
import org.sopt.havit.util.ERROR_OCCUR_TYPE
import org.sopt.havit.util.REPORT_CONTENT_TYPE
import org.sopt.havit.util.ToastUtil
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
        val id = intent?.getIntExtra(COMMUNITY_POST_ID, -1)
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
        }
        binding.ibMore.setOnSingleClickListener {
            communityDetailViewModel.communityPost.value?.isAuthor.let { isAuthor ->
                if (isAuthor == null) {
                    ToastUtil(this@CommunityDetailActivity).makeToast(
                        ERROR_OCCUR_TYPE
                    )
                } else if (isAuthor) {
                    showDeleteDialog(requireNotNull(communityDetailViewModel.communityPost.value?.id))
                } else {
                    showReportDialog(requireNotNull(communityDetailViewModel.communityPost.value?.id))
                }
            }
        }

    }

    private fun showReportDialog(id: Int) {
        val bottomSheet = BottomSheetReportFragment()
        bottomSheet.show(supportFragmentManager, BottomSheetReportFragment.TAG)

        bottomSheet.setReportClickListener(
            object : BottomSheetReportFragment.OnReportClickListener {
                override fun onClick() {
                    communityDetailViewModel.postCommunityReport(id)
                    ToastUtil(this@CommunityDetailActivity).makeToast(
                        REPORT_CONTENT_TYPE
                    )
                    val intent = Intent()
                    setResult(DETAIL_COMMUNITY, intent)
                    bottomSheet.dismiss()
                }
            })
    }

    private fun showDeleteDialog(id: Int) {
        val bottomSheet = BottomSheetDeleteFragment()
        bottomSheet.show(supportFragmentManager, BottomSheetDeleteFragment.TAG)

        bottomSheet.setDeleteClickListener(
            object : BottomSheetDeleteFragment.OnDeleteClickListener {
                override fun onClick() {
                    communityDetailViewModel.deleteCommunityPost(id)
                    ToastUtil(this@CommunityDetailActivity).makeToast(
                        CONTENT_DELETE_TYPE
                    )
                    bottomSheet.dismiss()
                    val intent = Intent()
                    setResult(DETAIL_COMMUNITY, intent)
                    finish()
                }
            })
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
