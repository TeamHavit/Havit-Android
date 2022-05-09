package org.sopt.havit.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard

class SettingModifyActivity :
    BaseBindingActivity<ActivitySettingModifyBinding>(R.layout.activity_setting_modify) {
    private val settingViewModel: SettingViewModel by lazy { SettingViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmSetting = settingViewModel

        setClickListener()
        setData()
        setNicknameLength()
        setUpAsSoftKeyboard(binding.root)
//        setKeyBoardUp()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener { finish() }
        binding.ivNicknameDelete.setOnClickListener { binding.etNickname.text.clear() }
        binding.btnComplete.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }

    // 닉네임 글자 수 세기
    private fun setNicknameLength() {
        binding.etNickname.doOnTextChanged { text, _, _, _ ->
            Log.d("SETTING ACTIVITY", "setTextWatcherCount : ${text?.length}")
            text?.length?.let {
                settingViewModel.setNicknameLength(it)
                binding.isClickable = it > 0
            }
        }
    }

//    private fun setKeyBoardUp() = KeyBoardUtil.openKeyBoard(this, binding.etNickname)
}
