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

        setToken()

        val fab: View = findViewById(R.id.save)
        fab.setOnClickListener { view ->
            SaveFragment("응가").show(supportFragmentManager, "save")
            //startActivity(Intent(this,SaveActivity::class.java))
        }

    }

    private fun setToken() {
        if (MySharedPreference.getXAuthToken(this) == ""){
            MySharedPreference.setXAuthToken(this)
        }
    }
}