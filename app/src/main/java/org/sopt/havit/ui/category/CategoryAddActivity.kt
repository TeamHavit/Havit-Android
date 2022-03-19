package org.sopt.havit.ui.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.havit.databinding.ActivityCategoryAddBinding

class CategoryAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startShareProcess()
    }
    private fun startShareProcess(){
        val bottomSheet = DialogCategoryAddFragment()
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }
}


