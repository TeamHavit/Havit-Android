package org.sopt.havit.ui.setting

import android.content.Intent
import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SettingModifyActivity :
    BaseBindingActivity<ActivitySettingModifyBinding>(R.layout.activity_setting_modify) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.clUnregister.setOnClickListener {
            startActivity(Intent(this, SettingUnregisterActivity::class.java))
        }
    }
}