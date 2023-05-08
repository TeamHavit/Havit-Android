package org.sopt.havit.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingBinding
import org.sopt.havit.domain.entity.VersionState
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.home.ServiceGuideActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.CANNOT_SEND_MAIL_TYPE
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.ERROR_OCCUR_TYPE
import org.sopt.havit.util.HavitSharedPreference
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSingleClickListener
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : BaseBindingActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val settingViewModel: SettingViewModel by viewModels()

    @Inject
    lateinit var preference: HavitSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmSetting = settingViewModel
        setClickListener()
        getLatestVersion()
        observeIsLatest()
        onClickUpdate()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun getLatestVersion() {
        settingViewModel.getLatestVersion()
    }

    private fun onClickUpdate() {
        binding.tvVersionStatus.setOnSingleClickListener {
            if (settingViewModel.versionState.value == VersionState.Update) {
                linkToPlayStore()
            }
        }
    }

    private fun linkToPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=$packageName")
                setPackage("com.android.vending")
            }
            startActivity(intent)
        } catch (e: Exception) {
            ToastUtil(this).makeToast(ERROR_OCCUR_TYPE)
        }
    }


    private fun setVersionLatestState() {
        with(binding.tvVersionStatus) {
            setText(R.string.latest_version)
            setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.gray_3))
        }
    }

    private fun setVersionUpdateState() {
        with(binding.tvVersionStatus) {
            setText(R.string.update)
            setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.havit_purple))
        }
    }

    private fun setVersionUnknownState() {
        with(binding.tvVersionStatus) {
            setText(R.string.dash)
            setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.gray_3))
        }
    }

    private fun observeIsLatest() {
        settingViewModel.versionState.observe(this) { versionState ->
            versionState ?: return@observe
            when (versionState) {
                VersionState.Latest -> setVersionLatestState()
                VersionState.Update -> setVersionUpdateState()
                VersionState.Unknown -> setVersionUnknownState()
            }
        }
    }


    private fun setClickListener() {
        // 뒤로가기
        binding.ivBack.setOnSingleClickListener { finish() }

        // 프로필 수정
        binding.ivEdit.setOnSingleClickListener {
            val intent = Intent(this, SettingModifyNicknameActivity::class.java)
            intent.putExtra(nickname, settingViewModel.user.value?.nickname)
            startActivity(intent)
        }

        // 알림 설정
        binding.clSettingAlarm.setOnSingleClickListener {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("app_package", packageName)
            intent.putExtra("app_uid", applicationInfo.uid)
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)

            startActivity(intent)
        }

        // 공지사항
        binding.clNotice.setOnSingleClickListener {
            startActivity(Intent(this, SettingNoticeActivity::class.java))
        }

        // 약관 및 정책
        binding.clPolicy.setOnSingleClickListener {
            startActivity(Intent(this, SettingPolicyActivity::class.java))
        }

        // 서비스 이용방법
        binding.clHowToUse.setOnSingleClickListener {
            startActivity(Intent(this, ServiceGuideActivity::class.java))
        }

        // 개인정보 처리 방침
        binding.clPersonalData.setOnSingleClickListener {
            startActivity(Intent(this, SettingPersonalDataActivity::class.java))
        }

        // 고객센터
        binding.tvCustomerCenter.setOnSingleClickListener { sendMail() }

        // 로그아웃
        binding.tvLogout.setOnSingleClickListener { showLogoutDialog() }

        // 회원 탈퇴
        binding.clUnregister.setOnSingleClickListener {
            startActivity(Intent(this, SettingUnregisterActivity::class.java))
        }
    }

    private fun showLogoutDialog() {
        val dialog = DialogUtil(DialogUtil.LOGOUT, ::logout)
        dialog.show(supportFragmentManager, this.javaClass.name)
    }

    private fun logout() {
        settingViewModel.removeHavitAuthToken()
        preference.clearXAuthToken()
        preference.saveFirstEnter()
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
            data = Uri.parse("mailto:havitofficial29@gmail.com")
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
