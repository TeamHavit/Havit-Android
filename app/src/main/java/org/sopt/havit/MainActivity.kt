package org.sopt.havit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.ActivityMainBinding
import org.sopt.havit.ui.category.CategoryFragment
import org.sopt.havit.ui.home.HomeFragment
import org.sopt.havit.ui.mypage.MyPageFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavi()
        setListeners()
    }

    private fun setBottomNavi() {
        // TODO GA붙이기 위해 임의로 막아놓은 코드
        //navView = binding.navView
        //navHostFragment =
        //    supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        //navController = navHostFragment.navController
        //navView.setupWithNavController(navController)
        binding.navView.background = null
    }

    private fun setListeners() {
        binding.floatingSave.bringToFront()
        binding.floatingSave.setOnClickListener {
            // 콘텐츠 추가 플로팅 버튼 클릭 시
            GoogleAnalyticsUtil.logClickEvent(GNB_ADD_CONTENT)
            SaveFragment("").show(supportFragmentManager, "save")
        }

        binding.navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    changeFragment(HomeFragment::class.java.name)
                    GoogleAnalyticsUtil.logClickEvent(GNB_HOME)
                }
                R.id.navigation_category -> {
                    changeFragment(CategoryFragment::class.java.name)
                    GoogleAnalyticsUtil.logClickEvent(GNB_CATEGORY)
                }
                R.id.navigation_my_page -> {
                    changeFragment(MyPageFragment::class.java.name)
                    GoogleAnalyticsUtil.logClickEvent(GNB_MYPAGE)
                }
                else -> {}
            }
            true
        }
    }

    private fun changeFragment(className: String) {
        val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, className)
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment).commit()
        //supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment).commit()
    }
}
