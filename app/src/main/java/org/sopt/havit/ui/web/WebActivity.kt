package org.sopt.havit.ui.web

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.animation.AnimationUtils
import android.webkit.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityWebBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.util.EventObserver
import org.sopt.havit.util.GoogleAnalyticsUtil
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_GO_BACK
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_REFRESH
import org.sopt.havit.util.GoogleAnalyticsUtil.CLICK_SHARE
import org.sopt.havit.util.GoogleAnalyticsUtil.CONTENT_SCREEN_TIME

@AndroidEntryPoint
class WebActivity : BaseBindingActivity<ActivityWebBinding>(R.layout.activity_web) {

    private val webViewModel: WebViewModel by viewModels()
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vm = webViewModel
        startTime = SystemClock.elapsedRealtime()
        initIsHavit()
        initHavitSeen()
        setUrlCheck()
        setListeners()
        initIsHavitObserver()
    }

    private fun initIsHavit() {
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
                    if (request?.url.toString().startsWith("towneers:")) {
                        startActivity(
                            Intent.parseUri(
                                request?.url.toString(),
                                Intent.URI_INTENT_SCHEME
                            )
                        )
                        finish()
                    }
                    return false
                }
            }
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            loadUrl(url)
        }
        webViewModel.setUrl(url)
    }

    private fun checkUrlNetwork(url: String) {
        if (URLUtil.isValidUrl(url)) {
            webViewModel.isServerNetwork.value = NetworkState.SUCCESS
            setUrlCheck()
        } else {
            webViewModel.isServerNetwork.value = NetworkState.FAIL
        }
    }

    private fun initIsHavitObserver() {
        webViewModel.isHavit.observe(this, EventObserver {
            if (it && webViewModel.isClick.value?.peekContent() == true) setCustomToast()
        })
    }

    private fun setListeners() {
        binding.llWebview.setOnClickListener {
            webViewModel.setHavit(intent!!.getIntExtra("contentsId", 0))
        }
        binding.ibWebBack.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_GO_BACK)
            finish()
        }
        binding.llWebShare.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_SHARE)
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra("url"))
            intentShare.type = "text/plain"
            startActivity(Intent.createChooser(intentShare, "앱을 선택해 주세요."))
        }
        binding.ibWebReload.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(CLICK_REFRESH)
            binding.wbCustom.reload()
        }
        binding.layoutNetworkError.ivRefresh.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.rotation_refresh
                )
            )
            checkUrlNetwork(requireNotNull(intent.getStringExtra("url")))
        }

    }

    private fun setCustomToast() {
        val toast = Toast(this)
        val view = layoutInflater.inflate(R.layout.toast_havit_complete, null)
        toast.view = view
        toast.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        super.finish()
        GoogleAnalyticsUtil.logClickEvent(CLICK_GO_BACK)
    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = SystemClock.elapsedRealtime()
        GoogleAnalyticsUtil.logScreenDurationTimeEvent(CONTENT_SCREEN_TIME, endTime - startTime)
    }

}
