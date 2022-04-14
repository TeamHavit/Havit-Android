package org.sopt.havit.ui.setting

import android.content.Intent
import android.os.Bundle
import org.sopt.havit.BuildConfig
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SettingActivity : BaseBindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingViewModel: SettingViewModel by lazy { SettingViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.vmSetting = settingViewModel
        setVersion()
        setClickListeners()
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setVersion() {
        val version = BuildConfig.VERSION_NAME  // build.gradle->defaultConfig에 있는 version_name
        settingViewModel.setVersion(version)
    }

    private fun setClickListeners() {
        // 뒤로가기
        binding.ivBack.setOnClickListener { finish() }
        // 내 정보 수정
        binding.ivEdit.setOnClickListener {
            startActivity(Intent(this, SettingModifyActivity::class.java))
        }
        // 알림 설정
        binding.clSettingAlarm.setOnClickListener {
            startActivity(Intent(this, SettingAlarmActivity::class.java))
        }
        // 공지사항
        binding.clNotice.setOnClickListener {
            startActivity(Intent(this, SettingNoticeActivity::class.java))
        }
        // 약관 및 정책
        binding.clPolicy.setOnClickListener {
            startActivity(Intent(this, SettingPolicyActivity::class.java).apply {
                putExtra("beforeActivity", "setting")
            })
        }
        // 서비스 이용방법
        binding.clHowToUse.setOnClickListener {
            // 나중에 추가
        }
        // 개인정보 처리 방침
        binding.clPersonalData.setOnClickListener {
            startActivity(Intent(this, SettingPersonalDataActivity::class.java).apply {
                putExtra("beforeActivity", "setting")
            })
        }
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }
}