package org.sopt.havit.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.sopt.havit.BuildConfig
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel
import org.sopt.havit.util.CustomToast

class SettingActivity : BaseBindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingViewModel: SettingViewModel by lazy { SettingViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.vmSetting = settingViewModel
        setVersion()
        setClickListener()
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setVersion() {
        val version = BuildConfig.VERSION_NAME // build.gradle->defaultConfig에 있는 version_name
        settingViewModel.setVersion(version)
    }

    private fun setClickListener() {
        // 뒤로가기
        binding.ivBack.setOnClickListener { finish() }

        // 프로필 수정
        binding.ivEdit.setOnClickListener {
            startActivity(Intent(this, SettingModifyActivity::class.java))
        }

        // 알림 설정
        binding.clSettingAlarm.setOnClickListener {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("app_package", packageName)
            intent.putExtra("app_uid", applicationInfo.uid)
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)

            startActivity(intent)
//            startActivity(Intent(this, SettingAlarmActivity::class.java))
        }

        // 공지사항
        binding.clNotice.setOnClickListener {
            CustomToast.showTextToast(this, "서비스 준비중입니다")
        }

        // 약관 및 정책
        binding.clPolicy.setOnClickListener {
            startActivity(Intent(this, SettingPolicyActivity::class.java))
        }

        // 서비스 이용방법
        binding.clHowToUse.setOnClickListener {
            // 나중에 추가
        }

        // 개인정보 처리 방침
        binding.clPersonalData.setOnClickListener {
            startActivity(Intent(this, SettingPersonalDataActivity::class.java))
        }

        // 고객센터
        binding.tvCustomerCenter.setOnClickListener { sendMail() }

        // 로그아웃
        binding.tvLogout.setOnClickListener {
            // 나중에 추가
        }

        // 회원 탈퇴
        binding.clUnregister.setOnClickListener {
            startActivity(Intent(this, SettingUnregisterActivity::class.java))
        }
    }

    private fun sendMail() {
        val intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("havitofficial29@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "박태준 멍청이")
            putExtra(Intent.EXTRA_TEXT, "박태준바보멍청이\n".repeat(10))
        }
        if (intent.resolveActivity(this.packageManager) != null) startActivity(intent)
        else CustomToast.showTextToast(this, "메일을 전송할 수 없습니다")
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }
}
