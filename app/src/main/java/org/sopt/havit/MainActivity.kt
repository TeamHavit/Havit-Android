package org.sopt.havit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.ActivityMainBinding
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.util.GoogleAnalyticsUtil
import org.sopt.havit.util.GoogleAnalyticsUtil.GNB_ADD_CONTENT
import org.sopt.havit.util.GoogleAnalyticsUtil.GNB_CATEGORY
import org.sopt.havit.util.GoogleAnalyticsUtil.GNB_HOME
import org.sopt.havit.util.GoogleAnalyticsUtil.GNB_MYPAGE

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    private var isInitialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavi()
        setListeners()
    }

    private fun setBottomNavi() {
        navView = binding.navView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        // 바텀네비게이션 클릭 시 ga 이벤트 보냄
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    // 처음 홈 화면 진입 시는 클릭해서 진입한게 아니므로 이벤트 보내지 않음
                    if (isInitialized) {
                        GoogleAnalyticsUtil.logClickEvent(GNB_HOME)
                    } else {
                        isInitialized = true
                    }
                }
                R.id.navigation_category -> {
                    GoogleAnalyticsUtil.logClickEvent(GNB_CATEGORY)
                }
                R.id.navigation_my_page -> {
                    GoogleAnalyticsUtil.logClickEvent(GNB_MYPAGE)
                }
                else -> {}
            }
        }
        navView.setupWithNavController(navController)
        binding.navView.background = null
    }

    private fun setListeners() {
        binding.floatingSave.bringToFront()
        binding.floatingSave.setOnClickListener {
            // 콘텐츠 추가 플로팅 버튼 클릭 시
            GoogleAnalyticsUtil.logClickEvent(GNB_ADD_CONTENT)
            SaveFragment("").show(supportFragmentManager, "save")
        }
    }
}
