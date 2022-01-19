package org.sopt.havit

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.sopt.havit.data.repository.ContentsRepository
import org.sopt.havit.data.repository.MyPageRepository
import org.sopt.havit.data.repository.SearchRepository
import org.sopt.havit.domain.repository.ContentsRepositoryImpl
import org.sopt.havit.domain.repository.SearchRepositoryImpl
import org.sopt.havit.ui.mypage.MyPageViewModel
import org.sopt.havit.ui.search.SearchViewModel
import org.sopt.havit.ui.web.WebViewModel

class MainApplication :Application() {

    companion object
    {
        var instance: MainApplication? = null
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
        viewModel { WebViewModel(get()) }
    }

    private val myModule = module{
        single { MyPageRepository() }
        single<SearchRepository> { SearchRepositoryImpl() }
        single<ContentsRepository>{ ContentsRepositoryImpl() }
    }

    override fun onTerminate()
    {
        super.onTerminate()
        instance = null
    }

    fun getMainApplicationContext(): org.sopt.havit.MainApplication
    {
        checkNotNull(instance) { "this application does not inherit com.kakao.MainApplication" }
        return instance!!
    }
}