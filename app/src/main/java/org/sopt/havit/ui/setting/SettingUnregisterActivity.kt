package org.sopt.havit.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingUnregisterBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel

@AndroidEntryPoint
class SettingUnregisterActivity :
    BaseBindingActivity<ActivitySettingUnregisterBinding>(R.layout.activity_setting_unregister) {
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmSetting = settingViewModel
        binding.isChecked = false

        setClickListener()
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setClickListener() {
        binding.ivBack.setOnClickListener { finish() }
        binding.ivCheck.setOnClickListener {
            binding.isChecked = binding.isChecked == false
        }
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }
}
