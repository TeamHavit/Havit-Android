package org.sopt.havit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.sopt.havit.databinding.ActivityMainBinding
import org.sopt.havit.ui.save.SaveActivity
import org.sopt.havit.ui.save.SaveFragment

class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setupWithNavController(navController)
        binding.navView.background = null
        navView.menu.getItem(3).isEnabled = false
        binding.save.bringToFront()

        val fab: View = findViewById(R.id.save)
        fab.setOnClickListener { view ->
            SaveFragment().show(supportFragmentManager, "save")
            //startActivity(Intent(this,SaveActivity::class.java))
        }

        bottomNavVisible()
    }

    private fun bottomNavVisible() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.contentsFragment) {
                navView.visibility = GONE
                with(binding) {
                    bottomAppBar.visibility = GONE
                    navHostFragmentActivityMain.layoutParams.height =
                        resources.displayMetrics.heightPixels
                    save.visibility = GONE
                }
            } else {
                navView.isVisible = true
                with(binding) {
                    bottomAppBar.isVisible = true
                    navHostFragmentActivityMain.layoutParams.height =
                        ViewGroup.LayoutParams.MATCH_PARENT
                    save.isVisible = true
                }
            }
        }
    }
}