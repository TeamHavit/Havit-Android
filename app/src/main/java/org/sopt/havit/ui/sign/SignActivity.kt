package org.sopt.havit.ui.sign

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySignBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.util.EventObserver

@AndroidEntryPoint
class SignActivity : BaseBindingActivity<ActivitySignBinding>(R.layout.activity_sign) {

    private val signInViewModel: SignViewModel by viewModels()
    private val signVpAdapter: SignVpAdapter by lazy { SignVpAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKakaoUserInfo()
        initSignViewPager()
        initIsMoveToNextOrBackObserver()
    }

    private fun initKakaoUserInfo() {
        signInViewModel.setFcmToken()
        signInViewModel.getKakaoToken()
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.d("TAG", "사용자 정보 요청 성공")
                val age = user.kakaoAccount?.ageRange.toString().split("_")
                signInViewModel.setKakaoUserInfo(
                    (age[1].toInt() + age[2].toInt()) / 2,
                    user.kakaoAccount?.email ?: "",
                    user.kakaoAccount?.gender.toString(),
                    user.kakaoAccount?.profile?.nickname ?: ""
                )
            }
        }
    }

    private fun initSignViewPager() {
        binding.vpSign.adapter = signVpAdapter
        binding.vpSign.isUserInputEnabled = false
    }

    private fun initIsMoveToNextOrBackObserver() {
        signInViewModel.isMoveToNextOrBack.observe(
            this,
            EventObserver {
                binding.vpSign.currentItem = if (it) 1 else 0
            }
        )
    }
}
