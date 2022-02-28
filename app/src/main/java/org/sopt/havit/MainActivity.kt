package org.sopt.havit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.sopt.havit.databinding.ActivityMainBinding
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.util.MySharedPreference

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
        setToken()

    }

    private fun setBottomNavi() {
        navView = binding.navView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id==R.id.navigation_my_page_to_category){
                binding.bottomAppBar.visibility= View.INVISIBLE
                navView.visibility=View.INVISIBLE
            }else{
                binding.bottomAppBar.visibility= View.VISIBLE
                navView.visibility=View.VISIBLE
            }
        }
        navView.setupWithNavController(navController)
        binding.navView.background = null
    }

    private fun setListeners() {
        binding.floatingSave.bringToFront()
        binding.floatingSave.setOnClickListener {
            SaveFragment("").show(supportFragmentManager, "save")
        }
    }

    private fun setToken() {
        if (MySharedPreference.getXAuthToken(this) == "") {
            MySharedPreference.setXAuthToken(this)
        }
    }
}