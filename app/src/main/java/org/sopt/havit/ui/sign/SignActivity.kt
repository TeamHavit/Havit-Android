package org.sopt.havit.ui.sign

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySignBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class SignActivity : BaseBindingActivity<ActivitySignBinding>(R.layout.activity_sign) {

    private val viewModel: SignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_sign) as NavHostFragment
        val navController = navHostFragment.navController
    }


}