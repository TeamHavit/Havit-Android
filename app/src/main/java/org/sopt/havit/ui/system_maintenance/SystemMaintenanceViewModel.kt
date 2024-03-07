package org.sopt.havit.ui.system_maintenance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import javax.inject.Inject

@HiltViewModel
class SystemMaintenanceViewModel @Inject constructor(
    private val systemMaintenanceRepository: SystemMaintenanceRepository,
) : ViewModel() {

    private val _isSystemMaintenance: MutableLiveData<Boolean> = MutableLiveData()
    val isSystemMaintenance: LiveData<Boolean> = _isSystemMaintenance

    private var _systemMaintenanceMessage: MutableLiveData<String?> = MutableLiveData()
    val systemMaintenanceMessage: LiveData<String?> = _systemMaintenanceMessage

    fun fetchIsSystemMaintenance() {
        viewModelScope.launch {
            _isSystemMaintenance.postValue(systemMaintenanceRepository.isSystemMaintenance())
        }
    }

    fun fetchSystemMaintenanceMessage() {
        viewModelScope.launch {
            val message = systemMaintenanceRepository.getSystemMaintenanceMessage()
                .replace("\\n", "\n")
            _systemMaintenanceMessage.postValue(message)
        }
    }
}