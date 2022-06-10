package org.sopt.havit.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.ActivityShareBinding
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.HavitAuthUtil
import org.sopt.havit.util.MySharedPreference

@AndroidEntryPoint
class ShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        HavitAuthUtil.isLoginNow { isLogin ->
            if (isLogin) startShareProcess()
            else moveToLogin()
        }
    }

    private fun moveToLogin() {
        val intent = Intent(this, SplashWithSignActivity::class.java)
        startActivity(intent)
    }

    private fun startShareProcess() {
        val bottomSheet = BottomSheetShareFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    override fun onDestroy() {
        super.onDestroy()
        MySharedPreference.clearNotificationTime(this)
        MySharedPreference.clearTitle(this)
    }
}
