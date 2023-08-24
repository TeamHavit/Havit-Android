package org.sopt.havit.ui.setting

import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingPersonalDataBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SettingPersonalDataActivity :
    BaseBindingActivity<ActivitySettingPersonalDataBinding>(R.layout.activity_setting_personal_data) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener { this.finish() }
    }
}
