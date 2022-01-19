package org.sopt.havit.ui.notification

import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityNotificationBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class NotificationActivity :
    BaseBindingActivity<ActivityNotificationBinding>(R.layout.activity_notification) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        clickBackBtn()
    }

    private fun clickBackBtn() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}