package org.sopt.havit.util

import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

object HavitAuthUtil {

    fun isLoginNow(auto: (Boolean) -> Unit) {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        auto(false) //로그인 필요
                    }
                } else {
                    auto(true) // 현재 로그인
                }
            }
        } else {
            auto(false) //로그인 필요
        }
    }

}