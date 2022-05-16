package org.sopt.havit.ui.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.data.remote.SignUpRequest
import org.sopt.havit.databinding.FragmentSignInBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.EventObserver
import org.sopt.havit.util.HavitAuthUtil
import org.sopt.havit.util.MySharedPreference

@AndroidEntryPoint
class SignInFragment : BaseBindingFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val signInViewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setListeners()
        isAlreadyUserObserver()
        isNeedScopesObserver()
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

    private fun startAddNickNameFragment() {
        findNavController().navigate(
            R.id.action_signInFragment_to_addNickNameFragment
        )
    }

    private fun setLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e("TAG", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        requireContext(),
                        callback = signInViewModel.kakaoLoginCallback
                    )
                } else if (token != null) {
                    Log.i("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    signInViewModel.setKakaoToken(token.accessToken)
                    signInViewModel.setFcmToken()
                    signInViewModel.checkNewUser()
                    signInViewModel.setNeedScopes()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireContext(),
                callback = signInViewModel.kakaoLoginCallback
            )
        }
    }

    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i("TAG", "사용자 정보 요청 성공")
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


    private fun isNeedNewScopes() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                var scopes = mutableListOf<String>()
                if (user.kakaoAccount?.emailNeedsAgreement == true) {
                    scopes.add("account_email")
                }
                if (user.kakaoAccount?.ageRangeNeedsAgreement == true) {
                    scopes.add("age_range")
                }
                if (user.kakaoAccount?.genderNeedsAgreement == true) {
                    scopes.add("gender")
                }
                if (scopes.count() > 0) {
                    Log.d("TAG", "사용자에게 추가 동의를 받아야 합니다.")
                    //scope 목록을 전달하여 카카오 로그인 요청
                    UserApiClient.instance.loginWithNewScopes(
                        requireContext(),
                        scopes
                    ) { token, error ->
                        if (error != null) {
                            Log.e("TAG", "사용자 추가 동의 실패", error)
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
        signInViewModel.isNeedScopes.observe(viewLifecycleOwner) {
            isNeedNewScopes()
        }
    }

    private fun isAlreadyUserObserver() {
        signInViewModel.isAlreadyUser.observe(viewLifecycleOwner, EventObserver { isAlreadyUser ->
            if (isAlreadyUser.data.isAlreadyUser) { // 기존 유저
                MySharedPreference.setXAuthToken(
                    requireContext(),
                    isAlreadyUser.data.accessToken ?: ""
                )
                startMainActivity()
            } else { // 신규 유저
                startAddNickNameFragment()
            }

        })
    }


}