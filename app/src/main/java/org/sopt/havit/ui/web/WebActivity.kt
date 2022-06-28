package org.sopt.havit.ui.web

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityWebBinding
import org.sopt.havit.ui.base.BaseBindingActivity

@AndroidEntryPoint
class WebActivity : BaseBindingActivity<ActivityWebBinding>(R.layout.activity_web) {

    private val webViewModel: WebViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vm = webViewModel
        initHavitSeen()
        setUrlCheck()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        webViewModel.init(intent.getBooleanExtra("isSeen", false))
    }

    private fun initHavitSeen() {
        webViewModel.init(intent.getBooleanExtra("isSeen", false))
        if (intent.getIntExtra("contentsId", -1) == -1) {
            binding.llWebBottom.isVisible = false
        }
        if (!intent.getBooleanExtra("isSeen", false)) {
            Glide.with(this).load(R.drawable.ic_contents_unread).into(binding.ivWebviewUnread)
            binding.tvWebviewUnread.text = "콘텐츠 확인 완료"
        } else {
            Glide.with(this).load(R.drawable.ic_contents_read_2).into(binding.ivWebviewUnread)
            binding.tvWebviewUnread.text = "콘텐츠 확인하기"
        }
    }

    private fun setUrlCheck() {
        intent.getStringExtra("url")?.let { setUrlLaunch(it) }
    }

    private fun setUrlLaunch(url: String) {
        if (url.contains("naver.me")) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse(url)
            startActivity(intent)
            finish()
        } else {
            binding.wbCustom.apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
            }
            binding.wbCustom.loadUrl(url)
            webViewModel.setUrl(url)
        }
    }

    private fun setListeners() {
        binding.llWebview.setOnClickListener {
            webViewModel.setHavit(intent!!.getIntExtra("contentsId", 0))
            if (webViewModel.isHavit.value == true) {
                setCustomToast()
            }
        }
        binding.ibWebBack.setOnClickListener {
            finish()
        }
        binding.llWebShare.setOnClickListener {
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra("url"))
            intentShare.type = "text/plain"
            startActivity(Intent.createChooser(intentShare, "앱을 선택 해 주세요."))
        }
        binding.ibWebReload.setOnClickListener {
            binding.wbCustom.reload()
        }

    }

    private fun setCustomToast() {
        val toast = Toast(this)
        val view = layoutInflater.inflate(R.layout.toast_havit_complete, null)
        toast.view = view
        toast.show()
    }

    override fun onBackPressed() {
        if (findViewById<WebView>(R.id.wb_custom).canGoBack()) {
            findViewById<WebView>(R.id.wb_custom).goBack()
        } else {
            finish()
        }
    }


}