package org.sopt.havit.ui.setting

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingAlarmBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingAlarmViewModel

@AndroidEntryPoint
class SettingAlarmActivity :
    BaseBindingActivity<ActivitySettingAlarmBinding>(R.layout.activity_setting_alarm) {
    private val viewModel: SettingAlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

//        observeSwitchState()
//        initClickListener()
    }
//
//    private fun initClickListener() {
//        binding.ivBack.setOnClickListener { super.onBackPressed() }
//    }
//
//    private fun observeSwitchState() {
//        /* 공지사항 */
//        viewModel.isContentsNotiActivated.observe(this) {
//            viewModel.updateContentsNotiPreference()
//        }
//
//        /* 콘텐츠 알림 */
//        viewModel.isTotalNotiActivated.observe(this) {
//            viewModel.setTotalNotiActivated()
//        }
//
//        binding.switchAppNoti.setOnClickListener {
//            val intent = Intent()
//            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//            intent.putExtra("app_package", packageName)
//            intent.putExtra("app_uid", applicationInfo.uid)
//
//            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
//
//            startActivity(intent)
//        }
}
