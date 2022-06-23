package org.sopt.havit.ui.setting

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySettingModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.setting.viewmodel.SettingViewModel
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.KeyBoardUtil.setUpAsSoftKeyboard

@AndroidEntryPoint
class SettingModifyActivity :
    BaseBindingActivity<ActivitySettingModifyBinding>(R.layout.activity_setting_modify) {
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmSetting = settingViewModel

        initNickname()
        setListener()
        setUpAsSoftKeyboard(binding.root)
        setKeyBoardUp()
    }

    // etNickname에 들어갈 nickname 초기화
    private fun initNickname() {
        val nickname = intent.getStringExtra(SettingActivity.nickname).toString()
        binding.etNickname.setText(nickname)    // etNickname에 nickname 저장
        binding.etNickname.setSelection(nickname.length) // 커서 맨 뒤로 위치시킴
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

    private fun setKeyBoardUp() = KeyBoardUtil.openKeyBoard(this, binding.etNickname)
}
