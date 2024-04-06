package org.sopt.havit.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import org.sopt.havit.ui.system_maintenance.SystemMaintenanceActivity


abstract class BaseBindingActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
) : AppCompatActivity() {
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
    }

    val systemMaintenanceObserver = Observer<Boolean> { isSystemMaintenance ->
        if (isSystemMaintenance) startSystemMaintenanceActivity()
    }

    private fun startSystemMaintenanceActivity() {
        startActivity(Intent(this, SystemMaintenanceActivity::class.java))
        finish()
    }
}

