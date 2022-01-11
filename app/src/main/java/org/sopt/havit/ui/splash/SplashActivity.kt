package org.wesopt.havit.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.havit.R
import com.example.havit.databinding.ActivitySplashBinding
import org.sopt.havit.databinding.ActivitySplashBinding

class SplashActivity :
    org.wesopt.havit.ui.base.BaseBindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFilterActivity()
    }

    private fun startFilterActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, org.wesopt.havit.MainActivity::class.java))
            finish()

        }, 1000)
    }
}