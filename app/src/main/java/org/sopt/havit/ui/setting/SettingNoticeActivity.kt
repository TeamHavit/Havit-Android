package org.sopt.havit.ui.setting

import android.os.Bundle
import androidx.activity.viewModels
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingNoticeBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel

class SettingNoticeActivity :
    BaseBindingActivity<ActivitySettingNoticeBinding>(R.layout.activity_setting_notice) {
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}