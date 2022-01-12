package org.sopt.havit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.havit.databinding.ActivityShareBinding
import org.sopt.havit.ui.share.BottomSheetShareFragment

class ShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        if (hadLogin()) {
            startShareProcess()
        } else {
            moveToLogin()
        }
    }

    private fun moveToLogin(){

    }

    private fun startShareProcess(){
        val bottomSheet = BottomSheetShareFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun hadLogin(): Boolean {
        return true
    }
}