package org.sopt.havit.ui.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.havit.BuildConfig
import org.sopt.havit.HavitFirebaseMessagingService.Companion.TAG
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val systemMaintenanceRepository: SystemMaintenanceRepository,
) : ViewModel() {

    private val _isSystemMaintenance: MutableLiveData<Boolean> = MutableLiveData()
    val isSystemMaintenance: LiveData<Boolean> = _isSystemMaintenance

    private val _isForcedUpdatedNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isForcedUpdatedNeeded: StateFlow<Boolean> = _isForcedUpdatedNeeded

    init {
        fetchIsSystemMaintenance()
        fetchIsForcedUpdated()
    }


    private fun fetchIsSystemMaintenance() {
        viewModelScope.launch {
            _isSystemMaintenance.postValue(systemMaintenanceRepository.isSystemMaintenance())
        }
    }

    private fun fetchIsForcedUpdated() {
        viewModelScope.launch {
            systemMaintenanceRepository.getForcedUpdateVer()
                .collect {
                    _isForcedUpdatedNeeded.value = it > BuildConfig.VERSION_CODE
                    Log.d(TAG, "fetchIsForcedUpdated: $it")
                }
        }
    }
}
