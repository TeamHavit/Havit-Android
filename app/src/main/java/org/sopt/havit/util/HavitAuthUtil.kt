package org.sopt.havit.util

import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

object HavitAuthUtil {

    fun isLoginNow(isInternetConnected: (Boolean) -> Unit?, isLogin: (Boolean) -> Unit) {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->

                if (tokenInfo == null) isInternetConnected(true)

                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        isLogin(false) // 로그인 필요
                    }
                } else {
                    isLogin(true) // 현재 로그인
                }
            }
        } else {
            isLogin(false) // 로그인 필요
        }
    }
}
