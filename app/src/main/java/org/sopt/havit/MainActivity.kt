package org.sopt.havit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.sopt.havit.databinding.ActivityMainBinding
import org.sopt.havit.ui.save.SaveFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setupWithNavController(navController)
        binding.navView.background=null
        navView.menu.getItem(3).isEnabled = false
        binding.save.bringToFront()

        val fab: View = findViewById(R.id.save)
        fab.setOnClickListener { view ->
            SaveFragment().show(supportFragmentManager,"sdf")
        }
    }
}