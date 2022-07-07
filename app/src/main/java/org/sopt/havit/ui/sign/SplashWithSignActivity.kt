package org.sopt.havit.ui.sign

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
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

@AndroidEntryPoint
class SplashWithSignActivity :
    BaseBindingActivity<ActivitySplashWithSignBinding>(R.layout.activity_splash_with_sign) {

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
        initWhereSplashComesFrom()
        setSplashView()
        setListeners()
        isAlreadyUserObserver()
        isNeedScopesObserver()
        isReadyUserObserver()
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
            }
        }

    private fun setListeners() {
        binding.btnKakaoLogin.setOnClickListener {
            setLogin()
        }
        binding.tvAnotherLogin.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(
                this,
                prompts = listOf(Prompt.LOGIN)
            ) { token, error ->
                if (error != null) {
                    Log.d("TAG", "로그인 실패", error)
                } else if (token != null) {
                    Log.d("TAG", "로그인 성공 ${token.accessToken}")
                }
            }
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

    private fun setLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.d("TAG", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        this,
                        callback = signInViewModel.kakaoLoginCallback
                    )
                } else if (token != null) {
                    Log.d("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    signInViewModel.setKakaoToken(token.accessToken)
                    signInViewModel.setFcmToken()
                    signInViewModel.setNeedScopes()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                this,
                callback = signInViewModel.kakaoLoginCallback
            )
        }
    }

    private fun getKakaoUserInfo() {
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
                signInViewModel.setReadyUser()
            }
        }
    }

    private fun isNeedNewScopes() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val scopes = mutableListOf<String>()
                if (user.kakaoAccount?.emailNeedsAgreement == true) {
                    scopes.add("account_email")
                }
                if (user.kakaoAccount?.ageRangeNeedsAgreement == true) {
                    scopes.add("age_range")
                }
                if (user.kakaoAccount?.genderNeedsAgreement == true) {
                    scopes.add("gender")
                }
                if (scopes.isNotEmpty()) {
                    Log.d("TAG", "사용자에게 추가 동의를 받아야 합니다.")
                    // scope 목록을 전달하여 카카오 로그인 요청
                    UserApiClient.instance.loginWithNewScopes(
                        this,
                        scopes
                    ) { token, error ->
                        if (error != null) {
                            Log.d("TAG", "사용자 추가 동의 실패", error)
                        } else {
                            Log.d("TAG", "allowed scopes: ${token!!.scopes}")
                            // 사용자 정보 재요청
                            getKakaoUserInfo()
                        }
                    }
                } else {
                    getKakaoUserInfo()
                }
            }
        }
    }

    private fun isNeedScopesObserver() {
        signInViewModel.isNeedScopes.observe(this) {
            isNeedNewScopes()
        }
    }

    private fun isReadyUserObserver() {
        signInViewModel.isReadyUser.observe(this) {
            signInViewModel.getSignIn()
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
                    startOnBoardingActivity()
                }
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
