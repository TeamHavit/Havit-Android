package org.sopt.havit

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "6069fb41c205cbbf8f213b32eec7f2c8")

    }

}