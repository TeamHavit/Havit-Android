package org.sopt.havit.ui.setting

import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingUnregisterBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel

class SettingUnregisterActivity :
    BaseBindingActivity<ActivitySettingUnregisterBinding>(R.layout.activity_setting_unregister) {
    private val settingViewModel: SettingViewModel by lazy { SettingViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmSetting = settingViewModel

        setClickListener()
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }
}
