package org.sopt.havit.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingNoticeBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.adapter.NoticeAdapter
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel

@AndroidEntryPoint
class SettingNoticeActivity :
    BaseBindingActivity<ActivitySettingNoticeBinding>(R.layout.activity_setting_notice) {
    private val viewModel: SettingViewModel by viewModels()
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initAdapter()
        setNoticeDataOnAdapter()
        getNoticeList()
    }

    private fun initAdapter() {
        binding.rvNotice.adapter = NoticeAdapter(::onClickNotice).also { noticeAdapter = it }
    }

    private fun setNoticeDataOnAdapter() {
        viewModel.noticeList.observe(this) {
            noticeAdapter.submitList(it)
        }
    }

    private fun onClickNotice(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        })
    }

    private fun getNoticeList() {
        viewModel.getNoticeList()
    }


}