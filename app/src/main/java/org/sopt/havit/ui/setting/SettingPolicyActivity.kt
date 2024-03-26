package org.sopt.havit.ui.setting

import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingPolicyBinding
import org.sopt.havit.ui.base.BaseActivity

class SettingPolicyActivity :
    BaseActivity<ActivitySettingPolicyBinding>(R.layout.activity_setting_policy) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivBack.setOnClickListener { finish() }
    }
}
