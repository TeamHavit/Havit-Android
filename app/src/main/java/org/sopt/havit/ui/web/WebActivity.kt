package org.sopt.havit.ui.web

import android.os.Bundle
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
    }

    override fun onBackPressed() {
        /*if (binding.llWebview.canGoBack())
        {
            binding.llWebview.goBack()
        }
        else
        {
            finish()
        }*/
    }


}