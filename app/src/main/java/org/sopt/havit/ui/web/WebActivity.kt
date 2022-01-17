package org.sopt.havit.ui.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityWebBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class WebActivity : BaseBindingActivity<ActivityWebBinding>(R.layout.activity_web) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.wbCustom.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        binding.wbCustom.loadUrl("https://www.naver.co.kr/")

    }
}