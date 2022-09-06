package org.sopt.havit.ui.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.ActivityShareBinding
import org.sopt.havit.ui.sign.SignInViewModel.Companion.SPLASH_FROM_SHARE
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.MySharedPreference

@AndroidEntryPoint
class ShareActivity : AppCompatActivity() {
    private val viewModel: ShareViewModel by viewModels()
    private lateinit var splashWithSignActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivityLauncher()
        makeSignIn()
        setUrlOnViewModel()
    }

    private fun initActivityLauncher() {
        splashWithSignActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                onSlashWithSignActivityFinish(it)
            }
    }

    private fun onSlashWithSignActivityFinish(result: ActivityResult) {
        when (result.resultCode) {
            Activity.RESULT_OK -> showBottomSheetShareFragment()
            else -> finish()
        }
    }

    private fun isEnterWithShareProcess(intent: Intent?): Boolean {
        // 공유하기 버튼으로 진입하면 return true
        // MainActivity 의 + 버튼으로 진입하면 return false
        return (intent?.action == Intent.ACTION_SEND) && (intent.type == "text/plain")
    }

    private fun makeSignIn() {
        viewModel.makeSignIn(
            internetError = { showBottomSheetNetworkErrorFragment() },
            onUnAuthorized = { moveToSplashWithSignActivity() },
            onAuthorized = { showBottomSheetShareFragment() }
        )
    }

    private fun moveToSplashWithSignActivity() {
        val intent = Intent(this, SplashWithSignActivity::class.java).apply {
            putExtra(WHERE_SPLASH_COME_FROM, SPLASH_FROM_SHARE)
        }
        splashWithSignActivityLauncher.launch(intent)
    }

    private fun showBottomSheetNetworkErrorFragment() {
        val bottomSheet = BottomSheetNetworkErrorFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun showBottomSheetShareFragment() {
        val bottomSheet = BottomSheetShareFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun setUrlOnViewModel() {
        val intent = this.intent
        val url =
            if (isEnterWithShareProcess(intent)) // 공유하기 버튼으로 진입시
                intent?.getStringExtra(Intent.EXTRA_TEXT).toString()
            else intent?.getStringExtra("url").toString() // MainActivity + 로 진입시
        viewModel.setUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        MySharedPreference.clearTitle(this)
    }

    companion object {
        const val WHERE_SPLASH_COME_FROM = "WHERE_SPLASH_COME_FROM"
    }
}
