package org.sopt.havit.ui.setting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingModifyNicknameBinding
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard
import org.sopt.havit.util.setOnSinglePostClickListener

@AndroidEntryPoint
class SettingModifyNicknameActivity :
    BaseActivity<ActivitySettingModifyNicknameBinding>(R.layout.activity_setting_modify_nickname) {
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmSetting = settingViewModel

        setListener()
        setUpAsSoftKeyboard(binding.root)
        setNicknameOnEditText()
        showWhitespaceWarningMessage()
    }

    // etNickname에 들어갈 nickname 초기화
    private fun setNicknameOnEditText() {
        val nickname = intent.getStringExtra(SettingActivity.nickname).toString()
        binding.etNickname.setText(nickname)
        binding.etNickname.requestFocus()
        binding.etNickname.setSelection(nickname.length) // 커서 맨 뒤로 위치시킴
    }

    private fun showWhitespaceWarningMessage() {
        binding.etNickname.addTextChangedListener {
            val nickname = binding.etNickname.text.toString()
            settingViewModel.setNickname(nickname)
            settingViewModel.isNickNameContainWhiteSpace()
            settingViewModel.isNicknameValid()
        }
    }

    private fun setListener() {
        // 뒤로 가기 버튼
        binding.ivBack.setOnClickListener { finish() }
        // 텍스트 삭제 버튼
        binding.ivNicknameDelete.setOnClickListener { binding.etNickname.text.clear() }
        // 완료 버튼
        binding.btnComplete.setOnSinglePostClickListener {
            settingViewModel.fetchNickname()
            finish()
        }
    }

}
