package org.sopt.havit.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val systemMaintenanceRepository: SystemMaintenanceRepository,
) : ViewModel() {


    private val _isSystemMaintenance: MutableLiveData<Boolean> = MutableLiveData()
    val isSystemMaintenance: LiveData<Boolean> = _isSystemMaintenance


    fun fetchIsSystemMaintenance() {
        viewModelScope.launch {
            _isSystemMaintenance.postValue(systemMaintenanceRepository.isSystemMaintenance())
        }
    }
}
