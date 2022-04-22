package org.sopt.havit.ui.sign

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySignBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SignActivity : BaseBindingActivity<ActivitySignBinding>(R.layout.activity_sign) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_sign) as NavHostFragment
        val navController = navHostFragment.navController
    }


}