package org.sopt.havit.ui.sign

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.qualifiers.ActivityContext
import org.sopt.havit.data.local.HavitAuthLocalPreferences
import javax.inject.Inject

class KakaoLoginService @Inject constructor(
    @ActivityContext private val context: Context,
    private val preferences: HavitAuthLocalPreferences
) {


    fun setLoginWithAccount(kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit) {
        UserApiClient.instance.loginWithKakaoAccount(
            context,
            prompts = listOf(Prompt.LOGIN),
            callback = kakaoLoginCallback
        )
    }

    fun setKakaoLogin(kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(
                context,
                callback = kakaoLoginCallback
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                context,
                callback = kakaoLoginCallback
            )
        }
    }

    fun getUserNeedNewScopes(isGetUserInfo: (Boolean) -> Unit) {
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
                    UserApiClient.instance.loginWithNewScopes(context, scopes) { token, error ->
                        if (error != null) {
                            Log.e("TAG", "사용자 추가 동의 실패", error)
                        } else {
                            Log.d("TAG", "allowed scopes: ${token!!.scopes}")

                            // 사용자 정보 재요청
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e("TAG", "사용자 정보 요청 실패", error)
                                    isGetUserInfo(false)
                                } else if (user != null) {
                                    Log.i("TAG", "사용자 정보 요청 성공")
                                    getUserInfo(user)
                                    isGetUserInfo(true)
                                }
                            }
                        }
                    }

                } else {
                    getUserInfo(user)
                    isGetUserInfo(true)
                }
            }
        }
    }

    private fun getUserInfo(user: User) {
        val age = user.kakaoAccount?.ageRange.toString().split("_")

        preferences.userEmail = user.kakaoAccount?.email.toString()
        preferences.userGender = user.kakaoAccount?.gender.toString()
        preferences.userAge = (age[1].toInt() + age[2].toInt()) / 2
        preferences.userNickName = user.kakaoAccount?.profile?.nickname ?: ""
    }


}
