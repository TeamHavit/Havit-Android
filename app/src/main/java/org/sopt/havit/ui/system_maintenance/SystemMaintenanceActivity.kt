package org.sopt.havit.ui.system_maintenance

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySystemMaintenanceBinding
import org.sopt.havit.ui.base.BaseBindingActivity

@AndroidEntryPoint
class SystemMaintenanceActivity :
    BaseBindingActivity<ActivitySystemMaintenanceBinding>(R.layout.activity_system_maintenance) {

    private val systemMaintenanceViewModel: SystemMaintenanceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = systemMaintenanceViewModel

        systemMaintenanceViewModel.fetchIsSystemMaintenance()
        systemMaintenanceViewModel.fetchSystemMaintenanceMessage()
    }
}