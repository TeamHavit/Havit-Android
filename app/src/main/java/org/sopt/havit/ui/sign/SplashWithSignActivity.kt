package org.sopt.havit.ui.sign

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySplashWithSignBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.onboarding.OnboardingActivity
import org.sopt.havit.ui.share.ShareActivity
import org.sopt.havit.ui.sign.SignInViewModel.Companion.SPLASH_NORMAL_FLOW
import org.sopt.havit.util.EventObserver
import org.sopt.havit.util.HavitAuthUtil
import org.sopt.havit.util.HavitSharedPreference
import org.sopt.havit.util.setOnSinglePostClickListener
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class SplashWithSignActivity :
    BaseBindingActivity<ActivitySplashWithSignBinding>(R.layout.activity_splash_with_sign) {

    @Inject
    lateinit var preference: HavitSharedPreference

    @Inject
    lateinit var kakaoLoginService: KakaoLoginService

    private val signInViewModel: SignInViewModel by viewModels()
    private var isFromShare by Delegates.notNull<Boolean>()

    private val REQUEST_PERMISSION_CODE = 0

    private val alphaLogoAnim by lazy {
        AnimationUtils.loadAnimation(this, R.anim.alpha_15_to_5_20000).apply {
            fillAfter = true
            isFillEnabled = true
        }
    }

    private val alphaLoginAnim by lazy {
        AnimationUtils.loadAnimation(this, R.anim.alpha_0_to_100_1500_delay_1000).apply {
            fillAfter = true
            isFillEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.main = signInViewModel
        initFcmToken()
        initSuccessKakaoLoginObserver()
        initWhereSplashComesFrom()
        setLoginGuideIfFromShare()
        setSplashView()
        setListeners()
        isAlreadyUserObserver()
    }

    private fun initFcmToken() {
        signInViewModel.initFcmToken()
    }

    private fun initSuccessKakaoLoginObserver() {
        signInViewModel.isSuccessKakaoLogin.observe(this, EventObserver { isSuccess ->
            if (isSuccess) {
                kakaoLoginService.getUserNeedNewScopes { isGetUserInfo ->
                    if (isGetUserInfo) signInViewModel.getSignIn()
                }
            } else {
                kakaoLoginService.setLoginWithAccount(signInViewModel.kakaoLoginCallback)
            }
        })
    }


    private fun initWhereSplashComesFrom() {
        isFromShare = intent.getBooleanExtra(
            ShareActivity.WHERE_SPLASH_COME_FROM, SPLASH_NORMAL_FLOW
        )
    }

    private fun setLoginGuideIfFromShare() {
        signInViewModel.setLoginGuideVisibility(isFromShare)
    }

    private fun setLoginAnimation() {
        binding.btnKakaoLogin.startAnimation(alphaLoginAnim)
        binding.tvAnotherLogin.startAnimation(alphaLoginAnim)
    }

    private fun setSplashView() {
        if (signInViewModel.loginGuidVisibility.value == false) {
            binding.ivSplashLogo.startAnimation(alphaLogoAnim)
            alphaLogoAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationRepeat(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    checkAlarmPermission()
                }
            })
        } else {
            setAutoLogin()
        }
    }

    private fun setAutoLogin() {
        HavitAuthUtil.isLoginNow({ isInternetConnected ->
            if (isInternetConnected) {
                signInViewModel.isServerNetwork.value = NetworkState.FAIL
            }
        }) { isLogin ->
            signInViewModel.isServerNetwork.value = NetworkState.SUCCESS
            if (isLogin && preference.getXAuthToken().isNotEmpty()) startMainActivity()
            else if (preference.isFirstEnter()) startOnBoardingActivity()
            else setLoginAnimation()
        }
    }

    private val splashWithLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            setLoginAnimation()
        }

    private fun setListeners() {
        binding.layoutNetworkError.ivRefresh.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.rotation_refresh
                )
            )
            setAutoLogin()
        }
        binding.btnKakaoLogin.setOnSinglePostClickListener {
            kakaoLoginService.setKakaoLogin(signInViewModel.kakaoLoginCallback)
        }
        binding.tvAnotherLogin.setOnClickListener {
            kakaoLoginService.setLoginWithAccount(signInViewModel.kakaoLoginCallback)
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    private fun startOnBoardingActivity() {
        splashWithLoginLauncher.launch(
            Intent(this, OnboardingActivity::class.java)
        )
    }

    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_PERMISSION_CODE
            )

        } else {
            setAutoLogin()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startOnBoardingActivity() //권한 설정 완료
                } else {
                    //권한 설정 취소
                    val intent: Intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                            Uri.parse("package:" + this.packageName)
                        )
                    startActivity(intent)
                    this.finish()
                }
            }
        }
    }

    private fun isAlreadyUserObserver() {
        signInViewModel.isAlreadyUser.observe(
            this,
            EventObserver { isAlreadyUser ->
                if (isAlreadyUser.data.isAlreadyUser == null) { // 기존 유저
                    preference.setXAuthToken(isAlreadyUser.data.accessToken ?: "")
                    if (isFromShare) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else startMainActivity()
                } else { // 신규 유저
                    startActivity(Intent(this, SignUpActivity::class.java))
                }
            }
        )
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
