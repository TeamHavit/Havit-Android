package org.sopt.havit.ui.web

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_contents_linear_min.view.*
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityWebBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import java.util.regex.Pattern

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
        binding.wbCustom.apply {
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (!(request?.url.toString().startsWith("towneers:"))) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            addCategory(Intent.CATEGORY_BROWSABLE)
                            addCategory(Intent.CATEGORY_DEFAULT)
                            data = Uri.parse(request?.url.toString())
                        }
                        startActivity(intent)
                        finish()
                    }
                    return true
                }
            }
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            loadUrl(url)
        }
        webViewModel.setUrl(url)
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

}