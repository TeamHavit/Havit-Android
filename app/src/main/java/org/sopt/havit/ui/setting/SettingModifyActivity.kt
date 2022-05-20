package org.sopt.havit.ui.setting

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard

class SettingModifyActivity :
    BaseBindingActivity<ActivitySettingModifyBinding>(R.layout.activity_setting_modify) {
    private val settingViewModel: SettingViewModel by lazy { SettingViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmSetting = settingViewModel

        initNickname()
        setListener()
        setUpAsSoftKeyboard(binding.root)
        setKeyBoardUp()
        setNicknameLength()
    }

    // etNickname에 들어갈 nickname 초기화
    private fun initNickname() {
        val nickname: String
        intent.let {
            nickname = it.getStringExtra(SettingActivity.nickname).toString()
        }
        binding.etNickname.setText(nickname)    // etNickname에 nickname 저장
        val length = nickname.length
        binding.etNickname.setSelection(length) // 커서 맨 뒤로 위치시킴
        settingViewModel.setNicknameLength(length)  // 글자 수 세기
        binding.isClickable = length > 0        // isClickable 값 저장
    }

    private fun setListener() {
        // 뒤로 가기 버튼
        binding.ivBack.setOnClickListener { finish() }
        // 텍스트 삭제 버튼
        binding.ivNicknameDelete.setOnClickListener { binding.etNickname.text.clear() }
        // 완료 버튼
        binding.btnComplete.setOnClickListener {
            settingViewModel.requestNewNickname(binding.etNickname.text.toString())
            finish()
        }
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
