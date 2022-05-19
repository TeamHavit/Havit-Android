package org.sopt.havit.ui.onboarding

import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityOnboardingBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class OnboardingActivity :
    BaseBindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewPager2Adapter()
        setIndicator()
    }

    private fun setViewPager2Adapter() {
        binding.vpOnboarding.adapter = OnboardingVpAdapter(this)
    }

    private fun setIndicator() {
        binding.indicatorOnboarding.setViewPager2(binding.vpOnboarding)
    }

    companion object {
        const val ONBOARDING_PAGE_NUM = 5
    }
}