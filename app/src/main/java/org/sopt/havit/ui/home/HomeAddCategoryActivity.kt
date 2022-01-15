package org.sopt.havit.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityHomeAddCategoryBinding
import org.sopt.havit.ui.share.AddCategoryFragment

class HomeAddCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAddCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAddCategoryBinding.inflate(layoutInflater)

        initAddCategoryFragment()
        setContentView(binding.root)
    }

    private fun initAddCategoryFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fcv_add_category, AddCategoryFragment())
            .commit()
    }
}