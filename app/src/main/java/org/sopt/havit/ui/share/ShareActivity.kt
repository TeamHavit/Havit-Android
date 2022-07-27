package org.sopt.havit.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.ActivityShareBinding
import org.sopt.havit.ui.sign.SignInViewModel.Companion.SPLASH_FROM_SHARE
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.HavitAuthUtil
import org.sopt.havit.util.MySharedPreference

@AndroidEntryPoint
class ShareActivity : AppCompatActivity() {
    private val viewModel: ShareViewModel by viewModels()

    private lateinit var binding: ActivityShareBinding
    private var makeLogin = false /*로그인 화면으로 넘기는 로직을 1회만 실행. 해당 로직이 없으면 로그인화면이 무한 반복 됨*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeSignIn()
        setUrlOnViewModel()
    }

    private fun setUrlOnViewModel() {
        val intent = this.intent
        val url =
            if (isEnterWithShareProcess(intent)) // 공유하기 버튼으로 진입시
                intent?.getStringExtra(Intent.EXTRA_TEXT).toString()
            else intent?.getStringExtra("url").toString() // MainActivity + 로 진입시
        viewModel.setUrl(url)
    }

    private fun isEnterWithShareProcess(intent: Intent?): Boolean {
        // 공유하기 버튼으로 진입하면 return true
        // MainActivity 의 + 버튼으로 진입하면 return false
        return (intent?.action == Intent.ACTION_SEND) && (intent.type == "text/plain")
    }

    override fun onResume() {
        super.onResume()
        startSavingContents()
    }

    private fun makeSignIn() {
        HavitAuthUtil.isLoginNow({ isInternetConnected ->
            if (isInternetConnected) finish()
        }) { isLogin ->
            if (!isLogin) moveToSplashWithSignActivity()
        }
    }

    private fun startSavingContents() {
        HavitAuthUtil.isLoginNow({ isInternetConnected ->
            if (isInternetConnected) finish()
        }) { isLogin ->
            if (isLogin) saveContents()
            else if (makeLogin) finish()
        }
    }

    override fun onPause() {
        super.onPause()
        HavitAuthUtil.isLoginNow({ isInternetConnected ->
            if (isInternetConnected) finish()
        }) { isLogin ->
            if (!isLogin) makeLogin = true
        }
    }

    private fun moveToSplashWithSignActivity() {
        startActivity(
            Intent(this, SplashWithSignActivity::class.java).apply {
                putExtra(WHERE_SPLASH_COME_FROM, SPLASH_FROM_SHARE)
            }
        )
    }

    private fun saveContents() {
        val bottomSheet = BottomSheetShareFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    override fun onDestroy() {
        super.onDestroy()
        MySharedPreference.clearTitle(this)
    }

    companion object {
        const val WHERE_SPLASH_COME_FROM = "WHERE_SPLASH_COME_FROM"
    }
}
