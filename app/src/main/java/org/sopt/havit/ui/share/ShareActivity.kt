package org.sopt.havit.ui.share

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityShareBinding
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.ui.sign.SignInViewModel.Companion.SPLASH_FROM_SHARE
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.INVALID_URL_TYPE
import org.sopt.havit.util.ToastUtil
import java.io.Serializable

@AndroidEntryPoint
class ShareActivity : BaseActivity<ActivityShareBinding>(R.layout.activity_share) {

    private val shareViewModel: ShareViewModel by viewModels()
    private lateinit var splashWithSignLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shareViewModel.fetchIsSystemMaintenance()
        observeSystemUnderMaintenance()
        setScreenOrientation()
        handleShareFlow()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setScreenOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initializeActivityResultLauncher() {
        splashWithSignLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                handleSplashActivityResult(it)
            }
    }

    private fun handleSplashActivityResult(result: ActivityResult) {
        when (result.resultCode) {
            Activity.RESULT_OK -> showShareBottomSheet()
            else -> finish()
        }
    }

    private fun handleShareFlow() {
        initiateSignIn()
        extractAndSetUrl()
        shareViewModel.setCrawlingContents()
    }

    private fun initiateSignIn() {
        shareViewModel.makeSignIn(
            internetError = { showNetworkErrorBottomSheet() },
            onUnAuthorized = { moveToSplashWithSign() },
            onAuthorized = { showShareBottomSheet() }
        )
    }

    private fun moveToSplashWithSign() {
        initializeActivityResultLauncher()
        val intent = Intent(this, SplashWithSignActivity::class.java).apply {
            putExtra(WHERE_SPLASH_COME_FROM, SPLASH_FROM_SHARE)
        }
        splashWithSignLauncher.launch(intent)
    }

    private fun showNetworkErrorBottomSheet() {
        val bottomSheet = BottomSheetNetworkErrorFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ON_NETWORK_ERROR_DISMISS, { initiateSignIn() } as Serializable)
            }
        }
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun showShareBottomSheet() {
        val bottomSheet = BottomSheetShareFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun extractAndSetUrl() {
        val url = getUrlFromExtra()
        try {
            checkUrlNotNull(url)
            shareViewModel.setUrl(url.toString())
        } catch (e: IllegalStateException) {
            onUrlInvalid()
        }
    }

    private fun getUrlFromExtra(): String? {
        return intent?.getStringExtra(Intent.EXTRA_TEXT) ?: intent?.getStringExtra("url")
    }

    private fun checkUrlNotNull(url: String?) {
        requireNotNull(url) { throw IllegalStateException() }
    }

    private fun onUrlInvalid() {
        ToastUtil(this).makeToast(INVALID_URL_TYPE)
        finish()
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }
    }

    private fun observeSystemUnderMaintenance() {
        shareViewModel.isSystemMaintenance.observe(this, systemMaintenanceObserver)
    }

    companion object {
        const val WHERE_SPLASH_COME_FROM = "WHERE_SPLASH_COME_FROM"
        const val ON_NETWORK_ERROR_DISMISS = "ON_NETWORK_ERROR_DISMISS"
    }
}
