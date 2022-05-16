package org.sopt.havit.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySplashBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.sign.SignActivity
import org.sopt.havit.util.HavitAuthUtil

class SplashActivity : BaseBindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setAutoLogin()
        startSignActivity()
    }

    private fun setAutoLogin() { // 자동 로그인.
        Handler(Looper.getMainLooper()).postDelayed({
            HavitAuthUtil.isLoginNow { auto ->
                if (auto) startMainActivity() else startSignActivity()
            }
        }, 3000)

    }

    private fun startSignActivity() {
        startActivity(Intent(this, SignActivity::class.java))
        finish()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}