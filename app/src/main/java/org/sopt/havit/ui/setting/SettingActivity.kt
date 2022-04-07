package org.sopt.havit.ui.setting

import android.content.Intent
import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SettingActivity : BaseBindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingViewModel: SettingViewModel by lazy { SettingViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.vmSetting = settingViewModel
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }
}