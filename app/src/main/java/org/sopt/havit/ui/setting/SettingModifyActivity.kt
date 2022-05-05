package org.sopt.havit.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
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

        setListener()
        setData()
        setNicknameLength()
        setUpAsSoftKeyboard(binding.root)
        setKeyBoardUp()
    }

    private fun setListener() {
        // 뒤로 가기 버튼
        binding.ivBack.setOnClickListener { finish() }
        // 텍스트 삭제 버튼
        binding.ivNicknameDelete.setOnClickListener { binding.etNickname.text.clear() }
        // 완료 버튼
        binding.btnComplete.setOnClickListener {
            settingViewModel.requestNewNickname(binding.etNickname.toString())
            startActivity(Intent(this, SettingActivity::class.java))
            finish()
        }
        // editText 커서 위치를 글자 맨 뒤로 지정
        binding.etNickname.addTextChangedListener {
            it?.let {
                binding.etNickname.setSelection(it.length)
            }
        }
    }

    private fun setData() {
        settingViewModel.requestUserInfo()
    }

    // 닉네임 글자 수 세기
    private fun setNicknameLength() {
        binding.etNickname.doOnTextChanged { text, _, _, _ ->
            text?.length?.let {
                settingViewModel.setNicknameLength(it)
                binding.isClickable = it > 0
            }
        }
    }

    private fun setKeyBoardUp() = KeyBoardUtil.openKeyBoard(this, binding.etNickname)
}
