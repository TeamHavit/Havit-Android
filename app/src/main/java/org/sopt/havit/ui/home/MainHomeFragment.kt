package org.sopt.havit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentMainHomeBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.home.community.CommunityFragment
import org.sopt.havit.ui.home.home.HomeFragment
import org.sopt.havit.ui.home.home.ServiceGuideActivity
import org.sopt.havit.ui.notification.NotificationActivity
import org.sopt.havit.util.GoogleAnalyticsUtil
import org.sopt.havit.util.HavitSharedPreference
import org.sopt.havit.util.setOnSingleClickListener
import javax.inject.Inject

@AndroidEntryPoint
class MainHomeFragment : BaseBindingFragment<FragmentMainHomeBinding>(R.layout.fragment_main_home) {
    private val viewModel: MainHomeViewModel by viewModels()
    private lateinit var viewPagerAdapter: MainHomeViewPagerAdapter

    @Inject
    lateinit var preference: HavitSharedPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initTabLayout()
        initView()
        observe()
        showNoticeCommunityDialog()
    }

    private fun initAdapter() {
        val fragmentList = listOf(HomeFragment(), CommunityFragment())
        viewPagerAdapter = MainHomeViewPagerAdapter(this)
        viewPagerAdapter.fragments.addAll(fragmentList)
        binding.vpMainHome.adapter = viewPagerAdapter

        binding.vpMainHome.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    HOME_FRAGMENT -> setCommunityTooltipVisibility(isVisible = false)
                    COMMUNITY_FRAGMENT -> setCommunityTooltipVisibility(isVisible = true)
                }
            }
        })
    }

    private fun initTabLayout() {
        val tabLabel = listOf("홈", "커뮤니티")
        TabLayoutMediator(binding.tlMainTab, binding.vpMainHome) { tab, position ->
            tab.text = tabLabel[position]
        }.attach()
    }

    private fun initView() {
        binding.ivAlarm.setOnSingleClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.ivServiceGuide.setOnSingleClickListener {
            GoogleAnalyticsUtil.logClickEvent(GoogleAnalyticsUtil.CLICK_HAVIT_SERVICE_GUIDE)
            val intent = Intent(requireActivity(), ServiceGuideActivity::class.java)
            startActivity(intent)
        }

        setCommunityTooltipVisibility()
        binding.ivCloseCommunityTooltip.setOnSingleClickListener {
            setCommunityTooltipVisibility()
            preference.setCommunityTooltipClosed()
        }

        binding.vpMainHome.isUserInputEnabled = false // 뷰페이저 스와이프 제거
    }

    private fun observe() {
        viewModel.notificationList.observe(viewLifecycleOwner) { data ->
            binding.hasNotification = data.isNotEmpty()
        }
    }

    private fun setCommunityTooltipVisibility(isVisible: Boolean = true) {
        binding.clCommunityTooltip.isVisible = (!preference.isCommunityTooltipClosed() && isVisible)
    }

    private fun showNoticeCommunityDialog() {
        val bottomSheet = BottomSheetNoticeCommunityFragment()
        bottomSheet.setStyle(STYLE_NORMAL, R.style.TransParentBottomSheetDialogTheme)
        bottomSheet.show(childFragmentManager, BottomSheetNoticeCommunityFragment.TAG)

        bottomSheet.setStartCommunityClickListener(
            object : BottomSheetNoticeCommunityFragment.OnStartCommunityClickListener {
                override fun onClick() {
                    bottomSheet.dismiss()
                    binding.tlMainTab.selectTab(binding.tlMainTab.getTabAt(COMMUNITY_FRAGMENT))
                }
            })
    }

    companion object {
        const val HOME_FRAGMENT = 0
        const val COMMUNITY_FRAGMENT = 1
    }
}
