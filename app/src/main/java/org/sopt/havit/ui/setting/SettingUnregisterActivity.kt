package org.sopt.havit.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingUnregisterBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.DialogOkUtil
import org.sopt.havit.util.MySharedPreference

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

    private fun setClickListener() {
        // 뒤로가기
        binding.ivBack.setOnClickListener { finish() }

        // 체크 버튼
        binding.clCheck.setOnClickListener {
            binding.isChecked = binding.isChecked == false
        }

        // 탈퇴 버튼
        binding.tvUnregisterShort.setOnClickListener {
            if (binding.isChecked == true) {
                val dialog = DialogOkUtil(DialogOkUtil.UNREGISTER, unregister())
                dialog.show(this.supportFragmentManager, this.javaClass.name)
            }
        }
    }

    private fun unregister(): () -> Unit = {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("SETTING", "연결 끊기 실패", error)
            } else {
                Log.i("SETTING", "연결 끊기 성공. SDK에서 토큰 삭제됨")
            }
        }

        settingViewModel.unregister()
        MySharedPreference.clearXAuthToken(this)
        settingViewModel.removeHavitAuthToken()
        MySharedPreference.saveFirstEnter(this)
        startActivity(Intent(this, SplashWithSignActivity::class.java))
        finishAffinity()
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }
}
