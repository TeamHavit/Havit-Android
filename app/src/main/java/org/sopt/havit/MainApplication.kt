package org.sopt.havit

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.wesopt.havit.ui.mypage.MyPageViewModel
import org.wesopt.havit.ui.search.SearchViewModel

class MainApplication :Application() {

    companion object
    {
        var instance: org.sopt.havit.MainApplication? = null
    }

    override fun onCreate()
    {
        super.onCreate()

        KakaoSdk.init(this, "6069fb41c205cbbf8f213b32eec7f2c8")
        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@MainApplication)
            // use modules
            modules(myViewModel, myModule)
        }
    }

    private val myViewModel = module {
        viewModel { SearchViewModel(get()) }
        viewModel { MyPageViewModel(get()) }

    }

    private val myModule = module{
        single { org.wesopt.havit.data.repository.MyPageRepository() }
        single { org.wesopt.havit.data.repository.SearchRepository() }
    }

    override fun onTerminate()
    {
        super.onTerminate()
        org.sopt.havit.MainApplication.Companion.instance = null
    }

    fun getMainApplicationContext(): org.sopt.havit.MainApplication
    {
        checkNotNull(org.sopt.havit.MainApplication.Companion.instance) { "this application does not inherit com.kakao.MainApplication" }
        return org.sopt.havit.MainApplication.Companion.instance!!
    }
}