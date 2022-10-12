package org.sopt.havit.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityOnboardingBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.sign.SplashWithSignActivity
import org.sopt.havit.util.HavitSharedPreference
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity :
    BaseBindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    @Inject
    lateinit var preference: HavitSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewPager2Adapter()
        setIndicator()
        checkLastOnboardingPage()
        setJoinBtnClickListener()
        setSkipBtnClickListener()
    }

    private fun setViewPager2Adapter() {
        binding.vpOnboarding.adapter = OnboardingVpAdapter(this)
    }

    private fun setIndicator() {
        binding.indicatorOnboarding.setViewPager2(binding.vpOnboarding)
    }

    private fun checkLastOnboardingPage() {
        binding.vpOnboarding.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.lastOnboarding = (position == 4)
            }
        })
    }

    private fun setJoinBtnClickListener() {
        binding.btnJoin.setOnClickListener {
            preference.saveFirstEnter()
            startSplashWithSignActivity()
        }
    }

    private fun setSkipBtnClickListener() {
        binding.tvSkip.setOnClickListener {
            preference.saveFirstEnter()
            startSplashWithSignActivity()
        }
    }

    private fun startSplashWithSignActivity() {
        val intent = Intent(this, SplashWithSignActivity::class.java)
        setResult(RESULT_FIRST_USER, intent)
        finish()
    }

    override fun onBackPressed() {
        // super.onBackPressed()    // 건너뛰기, 가입하기 버튼을 눌러야만
    }

    companion object {
        const val ONBOARDING_PAGE_NUM = 5
    }
}
