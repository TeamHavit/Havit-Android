package org.sopt.havit.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityHomeCategoryAllBinding
import org.sopt.havit.ui.category.CategoryFragment

class HomeCategoryAllActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeCategoryAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeCategoryAllBinding.inflate(layoutInflater)

        initCategoryFragment()
        setContentView(binding.root)
    }


    private fun initCategoryFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fcv_category_all, CategoryFragment())
            .commit()
    }
}