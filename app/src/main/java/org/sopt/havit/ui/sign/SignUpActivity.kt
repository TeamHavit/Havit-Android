package org.sopt.havit.ui.sign

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySignUpBinding
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.util.EventObserver

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val signUpViewModel: SignUpViewModel by viewModels()
    private val signVpAdapter: SignVpAdapter by lazy { SignVpAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFcmToken()
        initSignViewPager()
        initIsMoveToNextOrBackObserver()
    }

    private fun initFcmToken() {
        signUpViewModel.initFcmToken()
    }

    private fun initSignViewPager() {
        binding.vpSign.adapter = signVpAdapter
        binding.vpSign.isUserInputEnabled = false
    }

    private fun initIsMoveToNextOrBackObserver() {
        signUpViewModel.isMoveToNextOrBack.observe(
            this,
            EventObserver {
                binding.vpSign.currentItem = if (it) 1 else 0
            }
        )
    }
}
