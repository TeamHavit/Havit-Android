package org.sopt.havit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

@AndroidEntryPoint
class MainHomeFragment : BaseBindingFragment<FragmentMainHomeBinding>(R.layout.fragment_main_home) {
    private val viewModel: MainHomeViewModel by viewModels()
    private lateinit var viewPagerAdapter: MainHomeViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.getNotificationList()
        initAdapter()
        initTabLayout()
        initView()
        observe()
        return binding.root
    }

    private fun initAdapter() {
        val fragmentList = listOf(HomeFragment(), CommunityFragment())
        viewPagerAdapter = MainHomeViewPagerAdapter(this)
        viewPagerAdapter.fragments.addAll(fragmentList)
        binding.vpMainHome.adapter = viewPagerAdapter

        binding.vpMainHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.clCommunityTooltip.visibility = View.GONE
                    1 -> binding.clCommunityTooltip.visibility = View.VISIBLE
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
        binding.ivAlarm.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }
        binding.ivServiceGuide.setOnClickListener {
            GoogleAnalyticsUtil.logClickEvent(GoogleAnalyticsUtil.CLICK_HAVIT_SERVICE_GUIDE)
            val intent = Intent(requireActivity(), ServiceGuideActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observe() {
        viewModel.notificationList.observe(viewLifecycleOwner) { data ->
            binding.hasNotification = data.isNotEmpty()
        }
    }
}
