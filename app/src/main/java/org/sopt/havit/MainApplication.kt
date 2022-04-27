package org.sopt.havit

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    companion object {
        var instance: MainApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSdk.init(this, "6069fb41c205cbbf8f213b32eec7f2c8")

    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getMainApplicationContext(): MainApplication {
        checkNotNull(instance) { "this application does not inherit com.kakao.MainApplication" }
        return instance!!
    }
}