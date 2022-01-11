package org.sopt.havit.ui.kakao

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySignInBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SignInActivity : BaseBindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {


    private val viewModel: SignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        setListeners()
        setAutoLogin()


    }

    private fun setListeners() {
        binding.kakaoLoginButton.setOnClickListener {
            setLogin()
        }
    }

    private fun setAutoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()

            } else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, org.sopt.havit.MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }


    private fun setLogin() {
        if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
            LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (token != null) {

            UserApiClient.instance.me { user, _ ->

                if (user != null) {
                    val scopes = mutableListOf<String>()

                    if (user.kakaoAccount?.emailNeedsAgreement == true) {
                        scopes.add("account_email")
                    }
                    if (user.kakaoAccount?.birthdayNeedsAgreement == true) {
                        scopes.add("birthday")
                    }
                    if (user.kakaoAccount?.birthyearNeedsAgreement == true) {
                        scopes.add("birthyear")
                    }
                    if (user.kakaoAccount?.genderNeedsAgreement == true) {
                        scopes.add("gender")
                    }

                    var email = user.kakaoAccount?.email
                    if (email == null) {
                        LoginClient.instance.loginWithNewScopes(this, scopes) { _, error2 ->
                            email = token.scopes.toString()

                        }

                    }

                    Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, org.sopt.havit.MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()

                }


            }


        }
    }


}