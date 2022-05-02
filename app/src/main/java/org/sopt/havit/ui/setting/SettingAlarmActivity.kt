package org.sopt.havit.ui.setting

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingAlarmBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SettingAlarmActivity :
    BaseBindingActivity<ActivitySettingAlarmBinding>(R.layout.activity_setting_alarm) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.switchAppNoti.setOnClickListener {
            Log.d(TAG, "onCreate: app_noti ${binding.switchAppNoti.isChecked}")
        }

        binding.switchContentsNoti.setOnClickListener {
            Log.d(TAG, "onCreate: contents_noti ${binding.switchAppNoti.isChecked}")
        }
    }
}
