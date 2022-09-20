package org.sopt.havit.ui.setting

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingNoticeBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel

@AndroidEntryPoint
class SettingNoticeActivity :
    BaseBindingActivity<ActivitySettingNoticeBinding>(R.layout.activity_setting_notice) {
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        initAdapter()
//        setNoticeData()
        getNoticeList()
    }

    private fun getNoticeList() {
        viewModel.getNoticeList()
    }

    ////            startActivity(Intent(Intent.ACTION_VIEW).apply {
    ////                data = Uri.parse(NOTICE_URL)
    ////            })

}