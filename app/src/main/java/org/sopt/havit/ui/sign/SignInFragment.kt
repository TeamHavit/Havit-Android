package org.sopt.havit.ui.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSignInBinding
import org.sopt.havit.ui.base.BaseBindingFragment


class SignInFragment : BaseBindingFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        //setAutoLogin()
    }

    private fun setListeners() {
        binding.kakaoLoginButton.setOnClickListener {
            setLogin()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        activity?.finish()
    }

    private fun startAddNickNameFragment(nickName: String) {
        findNavController().navigate(
            SignInFragmentDirections.actionSignInFragmentToAddNickNameFragment(
                nickName
            )
        )
    }

    private fun setAutoLogin() { // 자동 로그인.
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                setLogin()
            } else if (tokenInfo != null) {
                startMainActivity()
            }
        }
    }


    private fun setLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) { // 카카오톡이 설치되어 있으면
            UserApiClient.instance.loginWithKakaoTalk(
                requireContext(),
                callback = callback
            ) // 카카오 로그인
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireContext(),
                callback = callback
            ) // 카카오 계정 로그인(웹)
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            val authErrorToastMessage = when (error.toString()) {
                AuthErrorCause.AccessDenied.toString() -> "접근이 거부 됨(동의 취소)"
                AuthErrorCause.InvalidClient.toString() -> "유효하지 않은 앱"
                AuthErrorCause.InvalidGrant.toString() -> "인증 수단이 유효하지 않아 인증할 수 없는 상태"
                AuthErrorCause.InvalidRequest.toString() -> "요청 파라미터 오류"
                AuthErrorCause.InvalidScope.toString() -> "유효하지 않은 scope ID"
                AuthErrorCause.Misconfigured.toString() -> "설정이 올바르지 않음(android key hash)"
                AuthErrorCause.ServerError.toString() -> "서버 내부 에러"
                AuthErrorCause.Unauthorized.toString() -> "앱이 요청 권한이 없음"
                else -> "기타 에러"
            }
            Toast.makeText(requireContext(), authErrorToastMessage, Toast.LENGTH_SHORT).show()
        } else if (token != null) {

            UserApiClient.instance.me { user, _ -> // 사용자의 정보를 가져오는 코드.

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

                    if (scopes.count() > 0) {
                        // 사용자 추가 정보 요청 코드.
                        UserApiClient.instance.loginWithNewScopes(
                            requireContext(),
                            scopes
                        ) { _, error ->
                            if (error != null) {
                                Log.e("PASS", "사용자 추가 정보 획득 로그인 실패", error)
                            } else {
                                UserApiClient.instance.me { user, _ -> // 사용자 정보 재요청.
                                    if (user != null) {
                                        startAddNickNameFragment(user.kakaoAccount?.profile?.nickname?:"")
                                    } // 로그인 성공.
                                }
                            }

                        }

                    } else {
                        startAddNickNameFragment(user.kakaoAccount?.profile?.nickname?:"")
                    }
                }
            }


        }
    }
}