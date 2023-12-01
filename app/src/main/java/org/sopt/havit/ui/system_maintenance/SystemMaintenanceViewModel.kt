package org.sopt.havit.ui.system_maintenance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import javax.inject.Inject

@HiltViewModel
class SystemMaintenanceViewModel @Inject constructor(
    private val systemMaintenanceRepository: SystemMaintenanceRepository,
) : ViewModel() {

    fun isSystemMaintenance(): Boolean {
        return systemMaintenanceRepository.isSystemMaintenance()
    }

    fun getSystemMaintenanceMessage(): String {
        return systemMaintenanceRepository.getSystemMaintenanceMessage()
    }
}