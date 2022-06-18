package org.sopt.havit.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.ActivityShareBinding
import org.sopt.havit.ui.sign.SignInViewModel.Companion.SPLASH_FROM_SHARE
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.HavitAuthUtil
import org.sopt.havit.util.MySharedPreference

@AndroidEntryPoint
class ShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareBinding
    private var makeLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeSignIn()
    }

    override fun onResume() {
        super.onResume()
        startSavingContents()
    }

    private fun makeSignIn() {
        HavitAuthUtil.isLoginNow { isLogin ->
            if (!isLogin) {
                moveToSplashWithSignActivity()
            }
        }
    }

    private fun startSavingContents() {
        HavitAuthUtil.isLoginNow { isLogin ->
            if (isLogin) saveContents()
            else if (makeLogin) finish()
        }
    }

    override fun onPause() {
        super.onPause()
        HavitAuthUtil.isLoginNow { isLogin ->
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
        MySharedPreference.clearNotificationTime(this)
        MySharedPreference.clearTitle(this)
    }

    companion object {
        const val WHERE_SPLASH_COME_FROM = "WHERE_SPLASH_COME_FROM"
    }
}
