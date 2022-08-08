package org.sopt.havit.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.BuildConfig
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.home.ServiceGuideActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.CANNOT_SEND_MAIL_TYPE
import org.sopt.havit.util.MySharedPreference
import org.sopt.havit.util.SERVICE_PREPARING_TYPE
import org.sopt.havit.util.ToastUtil

@AndroidEntryPoint
class SettingActivity : BaseBindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmSetting = settingViewModel
        setVersion()
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
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
            val intent = Intent(this, SettingModifyNicknameActivity::class.java)
            intent.putExtra(nickname, settingViewModel.user.value?.nickname)
            startActivity(intent)
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
            ToastUtil(this).makeToast(SERVICE_PREPARING_TYPE)
        }

        // 약관 및 정책
        binding.clPolicy.setOnClickListener {
            startActivity(Intent(this, SettingPolicyActivity::class.java))
        }

        // 서비스 이용방법
        binding.clHowToUse.setOnClickListener {
            startActivity(Intent(this, ServiceGuideActivity::class.java))
        }

        // 개인정보 처리 방침
        binding.clPersonalData.setOnClickListener {
            startActivity(Intent(this, SettingPersonalDataActivity::class.java))
        }

        // 고객센터
        binding.tvCustomerCenter.setOnClickListener { sendMail() }

        // 로그아웃
        binding.tvLogout.setOnClickListener { logout() }

        // 회원 탈퇴
        binding.clUnregister.setOnClickListener {
            startActivity(Intent(this, SettingUnregisterActivity::class.java))
        }
    }

    private fun logout() {
        settingViewModel.removeHavitAuthToken()
        MySharedPreference.clearXAuthToken(this)
        MySharedPreference.saveFirstEnter(this)
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("SETTING", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i("SETTING", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
        startActivity(Intent(this, SplashWithSignActivity::class.java))
        finishAffinity()
    }

    private fun sendMail() {
        val intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("havitofficial29@gmail.com"))
        }
        if (intent.resolveActivity(this.packageManager) != null) startActivity(intent)
        else ToastUtil(this).makeToast(CANNOT_SEND_MAIL_TYPE)
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }

    companion object {
        const val nickname = "nickname"
    }
}
