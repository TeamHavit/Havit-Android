package org.sopt.havit.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.havit.databinding.ActivityServiceGuideBinding

class ServiceGuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener { finish() }
    }
}