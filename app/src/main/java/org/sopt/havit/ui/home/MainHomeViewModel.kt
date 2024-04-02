package org.sopt.havit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.NotificationResponse
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.notification.NotificationActivity
import javax.inject.Inject

@HiltViewModel
class MainHomeViewModel @Inject constructor(
    private val havitApi: HavitApi
) : ViewModel() {
    // 알림 예정 콘텐츠
    private val _notificationList = MutableLiveData<List<NotificationResponse.NotificationData>>()
    val notificationList: LiveData<List<NotificationResponse.NotificationData>> = _notificationList

    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState>
        get() = _loadState

    fun getNotificationList() {
        viewModelScope.launch {
            kotlin.runCatching {
                havitApi.getNotification(NotificationActivity.before)
            }.onSuccess { response ->
                _notificationList.postValue(response.data)
                _loadState.value = NetworkState.SUCCESS
            }.onFailure {
                _loadState.value = NetworkState.FAIL
            }
        }
    }
}