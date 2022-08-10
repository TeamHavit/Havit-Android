package org.sopt.havit.ui.sign

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySplashWithSignBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.onboarding.OnboardingActivity
import org.sopt.havit.ui.share.ShareActivity
import org.sopt.havit.ui.sign.SignInViewModel.Companion.SPLASH_NORMAL_FLOW
import org.sopt.havit.util.EventObserver
import org.sopt.havit.util.HavitAuthUtil
import org.sopt.havit.util.MySharedPreference
import javax.inject.Inject

@AndroidEntryPoint
class SplashWithSignActivity :
    BaseBindingActivity<ActivitySplashWithSignBinding>(R.layout.activity_splash_with_sign) {

    @Inject
    lateinit var kakaoLoginService: KakaoLoginService

    private val signInViewModel: SignInViewModel by viewModels()
    private val alphaLogoAnim by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.alpha_15_to_5_20000
        ).apply {
            fillAfter = true
            isFillEnabled = true
        }
    }
    private val alphaLoginAnim by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.alpha_0_to_100_1500_delay_1000
        ).apply {
            fillAfter = true
            isFillEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.main = signInViewModel
        initFcmToken()
        initSuccessKakaoLoginOserver()
        initWhereSplashComesFrom()
        setSplashView()
        setListeners()
        isAlreadyUserObserver()
        isNeedScopesObserver()
    }

    private fun initFcmToken() {
        signInViewModel.initFcmToken()
    }

    private fun initSuccessKakaoLoginOserver() {
        signInViewModel.isSuccessKakaoLogin.observe(this, EventObserver {
            if (it) signInViewModel.getSignIn()
        })
    }


    private fun initWhereSplashComesFrom() {
        signInViewModel.setLoginGuideVisibility(
            intent.getBooleanExtra(
                ShareActivity.WHERE_SPLASH_COME_FROM,
                SPLASH_NORMAL_FLOW
            )
        )
    }

    private fun setLoginAnimation() {
        binding.btnKakaoLogin.startAnimation(
            alphaLoginAnim
        )
        binding.tvAnotherLogin.startAnimation(
            alphaLoginAnim
        )
    }

    private fun setSplashView() {
        if (signInViewModel.loginGuidVisibility.value == false) {
            binding.ivSplashLogo.startAnimation(alphaLogoAnim)
            alphaLogoAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationRepeat(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    setAutoLogin()
                }
            })
        } else {
            setAutoLogin()
        }
    }

    private fun setAutoLogin() {
        HavitAuthUtil.isLoginNow { isLogin ->
            if (isLogin) {
                startMainActivity()
            } else {
                if (MySharedPreference.isFirstEnter(this)) {
                    startOnBoardingActivity()
                } else setLoginAnimation()
            }
        }
    }

    private val splashWithLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_FIRST_USER) {
                setLoginAnimation()
            } else {
                setLoginAnimation()
            }
        }

    private fun setListeners() {
        binding.btnKakaoLogin.setOnClickListener { kakaoLoginService.setKakaoLogin(signInViewModel.kakaoLoginCallback) }
        binding.tvAnotherLogin.setOnClickListener {
            kakaoLoginService.setLoginWithAccount(
                signInViewModel.kakaoLoginCallback
            )
        }
    }

    private fun startMainActivity() {
        MySharedPreference.saveFirstEnter(this)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    private fun startOnBoardingActivity() {
        splashWithLoginLauncher.launch(
            Intent(
                this,
                OnboardingActivity::class.java
            )
        )
    }

    private fun isNeedScopesObserver() {
        signInViewModel.isNeedScopes.observe(this) {
            kakaoLoginService.getUserNeedNewScopes()
        }
    }

    private fun isAlreadyUserObserver() {
        signInViewModel.isAlreadyUser.observe(
            this,
            EventObserver { isAlreadyUser ->
                if (isAlreadyUser.data.isAlreadyUser == null) { // 기존 유저
                    MySharedPreference.setXAuthToken(
                        this,
                        isAlreadyUser.data.accessToken ?: ""
                    )
                    startMainActivity()
                } else { // 신규 유저
                    startActivity(Intent(this, SignUpActivity::class.java))
                }
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
