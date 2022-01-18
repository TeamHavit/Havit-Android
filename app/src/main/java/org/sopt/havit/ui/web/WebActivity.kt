package org.sopt.havit.ui.web

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import org.koin.android.viewmodel.ext.android.viewModel
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityWebBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class WebActivity : BaseBindingActivity<ActivityWebBinding>(R.layout.activity_web) {


    private val webViewModel: WebViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vm = webViewModel
        binding.wbCustom.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        binding.wbCustom.loadUrl("https://www.naver.co.kr/")
        setListeners()
    }

    private fun setListeners() {
        binding.llWebview.setOnClickListener {
            webViewModel.setHavit(3)
        }
        binding.ibWebBack.setOnClickListener {
            finish()
        }
        binding.llWebShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,"https://www.naver.co.kr/")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent,"앱을 선택하든 말든지"))
        }
        binding.ibWebReload.setOnClickListener {
            binding.wbCustom.reload()
        }
        /*binding.ibWebChrome.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.co.kr/"))
                intent.setPackage("com.android.chrome")
                startActivity(intent)

        }*/
    }

    override fun onBackPressed() {
        if (findViewById<WebView>(R.id.wb_custom).canGoBack())
        {
            findViewById<WebView>(R.id.wb_custom).goBack()
        }
        else
        {
            finish()
        }
    }


}