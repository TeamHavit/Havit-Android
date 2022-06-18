package org.sopt.havit.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.sopt.havit.ui.onboarding.OnboardingActivity.Companion.ONBOARDING_PAGE_NUM

class OnboardingVpAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = ONBOARDING_PAGE_NUM

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFirstFragment()
            1 -> OnboardingSecondFragment()
            2 -> OnboardingThirdFragment()
            3 -> OnboardingFourthFragment()
            else -> OnboardingFifthFragment()
        }
    }
}
